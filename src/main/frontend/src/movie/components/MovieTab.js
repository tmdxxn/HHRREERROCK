import React, { useState, useEffect } from 'react';
import axios from "axios";

function MovieTab({ movieId, movieDetail }) {
    const [activeTab, setActiveTab] = useState('reviews');
    const [totalReviews, setTotalReviews] = useState(0);
    const [averageRating, setAverageRating] = useState(0);
    const [reviews, setReviews] = useState([]);
    const [error, setError] = useState(null);
    const [newReview, setNewReview] = useState({ content: '', rating: 5 });
    const [reviewContent, setReviewContent]= useState('')
    const [editingReview, setEditingReview] = useState(null);
    const [editingReviewId, setEditingReviewId] = useState(null);
    const [correspondMember, setCorrespondMember] = useState(null);
    const [correspondMemNum, setCorrespondMemNum] = useState(0);
    const [memRole, setMemRole] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageNumbers, setPageNumbers] = useState([]);
    const [hasPrevious, setHasPrevious] = useState(false);
    const [hasNext, setHasNext] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            fetchMemberInfo(token).then(info => {
                console.log('Fetched member info:', info);
                setMemRole(info.role);
                setCorrespondMember(info.memName);
                setCorrespondMemNum(info.memNum);
                console.log('Set memNum:', info.memNum, 'Set role:', info.role);
            }).catch(error => {
                console.error('Failed to fetch member info:', error);
            });
            fetchReviews(token, 1);
        }
    }, [movieId]);

    const fetchMemberInfo = async (token) => {
        try {
            const response = await axios.get('/auth/memberinfo', {
                headers: {'Authorization': `Bearer ${token}`}
            });
            console.log('Full member info response:', response.data);
            return {
                role: response.data.memRole,
                memName: response.data.memName,
                memNum: response.data.memNum
            };
        } catch (error) {
            console.error('사용자 정보를 가져오는 중 오류 발생:', error);
            throw error;
        }
    };

    const fetchReviews = async (token, page = 1) => {
        try {
            // const response = await axios.get(`/user/movies/detail/${movieId}/reviews`, {
            //     headers: { 'Authorization': `Bearer ${token}` }
            // });
            const response = await axios.get(`/user/movies/detail/${movieId}/reviews?page=${page}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            console.log('Fetched review data:', response.data);
            setReviews(response.data.reviews);
            setTotalReviews(response.data.totalReviews);
            setAverageRating(response.data.averageRating);
            setCurrentPage(response.data.currentPage);
            setPageNumbers(response.data.pageNumbers);
            setHasPrevious(response.data.hasPrevious);
            setHasNext(response.data.hasNext);
        } catch (error) {
            console.error('리뷰를 가져오는 중 오류 발생:', error);
            setError('리뷰를 불러오는데 실패했습니다.');
        }
    };

    const handleReviewCharsWithLimit = (e) => {
        if (e.target.value.length < 50) {
            setReviewContent(e.target.value);
        }
    };

    const handleEditClick = (review) => {
        setNewReview({
            content: review.reviewContent,
            rating: review.reviewRating
        });
        setEditingReviewId(review.reviewId);
    };

    const handleSubmitReview = async () => {
        const token = localStorage.getItem('accessToken');
        try {
            if (editingReviewId) {
                await axios.put(`/user/movies/detail/${movieId}/reviews/${editingReviewId}`, {
                    reviewContent: newReview.content,
                    reviewRating: newReview.rating
                }, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                setEditingReviewId(null);
            } else {
                await axios.post(`/user/movies/detail/${movieId}/reviews`, {
                    reviewContent: newReview.content,
                    reviewRating: newReview.rating
                }, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
            }
            setNewReview({ content: '', rating: 0 });
            await fetchReviews(token, currentPage);
            alert(editingReviewId ? '리뷰가 수정되었습니다.' : '리뷰가 작성되었습니다.');
        } catch (error) {
            console.error('리뷰 작성/수정 중 오류 발생:', error);
            setError('리뷰 작성/수정에 실패했습니다. 다시 시도해주세요.');
        }
    };

    const handleDeleteReview = async (reviewId) => {
        const token = localStorage.getItem('accessToken');
        try {
            await axios.delete(`/user/movies/detail/${movieId}/reviews/${reviewId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            await fetchReviews(token, currentPage);
            alert('리뷰가 삭제되었습니다.');
        } catch (error) {
            console.error('리뷰 삭제 중 오류 발생:', error);
            setError('리뷰 삭제에 실패했습니다. 다시 시도해주세요.');
        }
    };

    return (
        <>
            <div className='tabDiv'>
                <div className="tabs">
                    <button onClick={() => setActiveTab('reviews')}
                            className={activeTab === 'reviews' ? 'active' : ''}>리뷰 ({totalReviews})
                    </button>
                    <button onClick={() => setActiveTab('trailer')}
                            className={activeTab === 'trailer' ? 'active' : ''}>예고편
                    </button>
                </div>
                {activeTab === 'reviews' && (
                    <div className="tabDiv">
                        {/*<h2>리뷰 ({totalReviews})</h2>*/}
                        <p>평균 평점: {averageRating.toFixed(1)}/5</p>
                        {error && <p className="error">{error}</p>}
                        {/*상황별 에러 수정해야함*/}
                        <form
                            className="review-input"
                            onSubmit={(e) => {
                                e.preventDefault();
                                handleSubmitReview();
                            }}
                        >
                            <textarea
                                value={newReview.content}
                                onChange={handleReviewCharsWithLimit}
                                onChange={(e) => setNewReview({...newReview, content: e.target.value})}
                                placeholder="리뷰를 작성해주세요. (50자 이내)"
                                required
                            />
                            <input
                                type="number"
                                value={newReview.rating}
                                onChange={(e) => setNewReview({...newReview, rating: parseInt(e.target.value)})}
                                min="1"
                                max="5"
                                required
                            />
                            <button type="submit">
                                {editingReviewId ? '리뷰 수정' : '리뷰 작성'}
                            </button>
                            {editingReviewId && (
                                <button
                                    type="button"
                                    onClick={() => {
                                        setEditingReviewId(null);
                                        setNewReview({content: '', rating: 5});
                                    }}
                                >
                                    취소
                                </button>
                            )}
                        </form>
                        {/*<div className="review-input">*/}
                        {/*    <textarea*/}
                        {/*        value={newReview.content}*/}
                        {/*        onChange={(e) => setNewReview({...newReview, content: e.target.value})}*/}
                        {/*        placeholder="리뷰를 작성해주세요"*/}
                        {/*    />*/}
                        {/*    <input*/}
                        {/*        type="number"*/}
                        {/*        value={newReview.rating}*/}
                        {/*        onChange={(e) => setNewReview({...newReview, rating: parseInt(e.target.value)})}*/}
                        {/*        min="1"*/}
                        {/*        max="5"*/}
                        {/*    />*/}
                        {/*    <button onClick={handleSubmitReview}>*/}
                        {/*        {editingReviewId ? '리뷰 수정' : '리뷰 작성'}*/}
                        {/*    </button>*/}
                        {/*    {editingReviewId && (*/}
                        {/*        <button onClick={() => {*/}
                        {/*            setEditingReviewId(null);*/}
                        {/*            setNewReview({ content: '', rating: 0 });*/}
                        {/*        }}>*/}
                        {/*            취소*/}
                        {/*        </button>*/}
                        {/*    )}*/}
                        {/*</div>*/}
                        {reviews.length > 0 ? (
                            <>
                                <ul className="review_ul">
                                    {reviews.map((review) => (
                                        <li key={review.reviewId} className="review-item">
                                            {console.log('Review:', review, 'correspondMemNum:', correspondMemNum, 'memRole:', memRole)}
                                            <span className="reviewWriter">{review.memName}</span>
                                            <span className="reviewContent">{review.reviewContent}</span>
                                            <span className="reviewTime">
                                            {review.modifyDate && review.modifyDate !== review.createDate
                                                ? `수정됨: ${review.modifyDate}`
                                                : `작성: ${review.createDate}`}
                                        </span>
                                            <span className="reviewStar">{review.reviewRating}/5</span>
                                            <div className="review_actions">
                                                {(correspondMemNum && Number(review.memNum) === Number(correspondMemNum)) && (
                                                    <button onClick={() => handleEditClick(review)}>수정</button>
                                                )}
                                                {((memRole === 'ADMIN') || (correspondMemNum && Number(review.memNum) === Number(correspondMemNum))) && (
                                                    <button
                                                        onClick={() => handleDeleteReview(review.reviewId)}>삭제</button>
                                                )}
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                                <div className="pagination">
                                    {hasPrevious && (
                                        <button
                                            onClick={() => fetchReviews(localStorage.getItem('accessToken'), currentPage - 1)}>
                                            &lt;
                                        </button>
                                    )}
                                    {pageNumbers && pageNumbers.map(number => (
                                        <button
                                            key={number}
                                            onClick={() => fetchReviews(localStorage.getItem('accessToken'), number)}
                                            className={number === currentPage ? 'active' : ''}
                                        >
                                            {number}
                                        </button>
                                    ))}
                                    {hasNext && (
                                        <button
                                            onClick={() => fetchReviews(localStorage.getItem('accessToken'), currentPage + 1)}>
                                            &gt;
                                        </button>
                                    )}
                                </div>
                            </>
                        ) : (
                            <p>아직 리뷰가 없습니다. 첫 번째 리뷰를 작성해보세요!</p>
                        )}
                    </div>
                )}
                {activeTab === 'trailer' && (
                    <div className="trailer">
                        {movieDetail.trailers && movieDetail.trailers.length > 0 ? (
                            <video src={movieDetail.trailers[0].trailerUrl} controls>
                                Your browser does not support the video tag.
                            </video>
                        ) : (
                            <p>예고편이 없습니다.</p>
                        )}
                    </div>
                )}
            </div>
        </>
    );
}

export default MovieTab;