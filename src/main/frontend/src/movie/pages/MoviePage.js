import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

import '../../common/css/MoviePage.css';
import MovieTab from "../components/MovieTab";

function MoviePage() {
    const [movieDetail, setMovieDetail] = useState(null);
    const [isFavorite, setIsFavorite] = useState(false);
    const [totalFavorites, setTotalFavorites] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [memRole, setMemRole] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const { movieId } = useParams();

    useEffect(() => {
        if(error) {
            alert(error);
        }
    }, [error]);

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                alert("로그인이 필요합니다.");
                navigate('/login');
                return;
            }
            try {
                const memberInfo = await fetchMemberInfo(token);
                setMemRole(memberInfo.role);
                await fetchMovieDetail(token, movieId);
                // await fetchReviews(token, movieId);
                await checkFavoriteStatus(token);
                setIsLoading(false);
            } catch (error) {
                console.error("데이터 로딩 중 오류 발생:", error);
                setIsLoading(false);
            }
        };
        fetchData();
    }, [movieId, navigate]);

    const fetchMemberInfo = async (token) => {
        try {
            const response = await axios.get('/auth/memberinfo', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            return response.data;
        } catch (error) {
            console.error('사용자 정보를 가져오는 중 오류 발생:', error);
            throw error;
        }
    };

    const fetchMovieDetail = async (token) => {
        try {
            const response = await axios.get(`/user/movies/detail/${movieId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            setMovieDetail(response.data);
            console.log("영화정보", response.data);
            setError(''); // 성공 시 에러 메시지 초기화
        } catch (error) {
            console.error('영화 상세 정보를 가져오는 중 오류 발생:', error);
            setMovieDetail(null);

            if (error.response) {
                switch (error.response.data.errCode) {
                    case "ERR_R_RATED_MOVIE":
                        setError("청소년 관람 불가 등급의 영화입니다.");
                        break;
                    case "ERR_UNAUTHORIZED":
                        setError("접근 권한이 없습니다.");
                        navigate('/login');
                        break;
                    case "ERR_MEMBER_NOT_FOUND":
                        setError("회원 정보를 찾을 수 없습니다.");
                        navigate('/login');
                        break;
                    case "ERR_MOVIE_NOT_FOUND":
                        setError("영화를 찾을 수 없습니다.");
                        break;
                    default:
                        setError("영화 정보를 불러오는 데 실패했습니다.");
                }
            } else {
                setError("서버와의 연결에 실패했습니다.");
            }
        }
    };

    // const fetchReviews = async (token) => {
    //     try {
    //         const response = await axios.get(`/user/movies/${movieId}/reviews`, {
    //             headers: { 'Authorization': `Bearer ${token}` }
    //         });
    //         setReviews(response.data.reviews);
    //         setTotalReviews(response.data.totalReviews);
    //         setAverageRating(response.data.averageRating);
    //     } catch (error) {
    //         console.error('리뷰를 가져오는 중 오류 발생:', error);
    //         throw error;
    //     }
    // };

    // const checkFavoriteStatus = async (token) => {
    //     try {
    //         const response = await axios.get(`/user/movies/detail/${movieId}/favorites`, {
    //             headers: { 'Authorization': `Bearer ${token}` }
    //         });
    //         setIsFavorite(response.data.isFavorite);
    //         setTotalFavorites(response.data.favorCount);
    //     } catch (error) {
    //         console.error('찜 상태를 확인하는 중 오류 발생:', error);
    //         setIsFavorite(false);
    //         setTotalFavorites(0);
    //     }
    // };

    const checkFavoriteStatus = async (token) => {
        try {
            const response = await axios.get(`/user/movies/detail/${movieId}/favorites`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            console.log("Favorite status response:", response.data);
            setIsFavorite(response.data.favorite);
            setTotalFavorites(response.data.favorCount);
        } catch (error) {
            console.error('찜 상태를 확인하는 중 오류 발생:', error);
            setIsFavorite(false);
            setTotalFavorites(0);
        }
    };

    const toggleFavorite = async () => {
        const token = localStorage.getItem('accessToken');
        try {
            setIsFavorite(!isFavorite);
            setTotalFavorites(prev => isFavorite ? prev - 1 : prev + 1);

            let response;
            if (isFavorite) {
                await axios.delete(`/user/movies/detail/${movieId}/favorites`, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
            } else {
                await axios.post(`/user/movies/detail/${movieId}/favorites`, { movieId: movieId }, {
                    headers: { 'Authorization': `Bearer ${token}` }
                });
            }

            if (response && response.data) {
                setIsFavorite(response.data.isFavorite);
                setTotalFavorites(response.data.favorCount)
            } else {
                setIsFavorite(!isFavorite);
            }

        } catch (error) {
            console.error('찜하기 토글 중 오류 발생:', error);
            if (error.response && error.response.data) {
                alert(error.response.data.message || "찜하기 처리 중 오류가 발생했습니다.");
            } else {
                alert( "찜하기 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
            }
        }
    };

    // const toggleFavorite = async () => {
    //     const token = localStorage.getItem('accessToken');
    //     try {
    //         let response;
    //         if (isFavorite) {
    //             response = await axios.delete(`/user/movies/detail/${movieId}/favorites`, {
    //                 headers: { 'Authorization': `Bearer ${token}` }
    //             });
    //         } else {
    //             response = await axios.post(`/user/movies/detail/${movieId}/favorites`, { movieId: movieId }, {
    //                 headers: { 'Authorization': `Bearer ${token}` }
    //             });
    //         }
    //
    //         if (response && response.data) {
    //             setIsFavorite(response.data.isFavorite);
    //             setTotalFavorites(response.data.favorCount);
    //         } else {
    //             console.error('서버 응답에 예상된 데이터가 없습니다.');
    //             // 서버 응답이 없는 경우, 클라이언트에서 상태를 토글합니다.
    //             setIsFavorite(!isFavorite);
    //             setTotalFavorites(prev => isFavorite ? Math.max(0, prev - 1) : prev + 1);
    //         }
    //     } catch (error) {
    //         console.error('찜하기 토글 중 오류 발생:', error);
    //         if (error.response && error.response.data) {
    //             alert(error.response.data.message || "찜하기 처리 중 오류가 발생했습니다.");
    //         } else {
    //             alert("찜하기 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
    //         }
    //     }
    // };
    // const handleSubmitReview = async (e) => {
    //     e.preventDefault();
    //     const token = localStorage.getItem('accessToken');
    //     try {
    //         await axios.post(`/movies/${movieId}/reviews`, newReview, {
    //             headers: { 'Authorization': `Bearer ${token}` }
    //         });
    //         setNewReview({ content: '', rating: 0 });
    //         await fetchReviews(token); // 리뷰 작성 후 리뷰 목록을 다시 불러오도록 수정
    //     } catch (error) {
    //         console.error('리뷰 작성 중 오류 발생:', error);
    //         throw error;
    //     }
    // };

    // const handleReviewAction = async (reviewId, action) => {
    //     const token = localStorage.getItem('accessToken');
    //     try {
    //         if (action === 'delete') {
    //             await axios.delete(`/movies/${movieId}/reviews/${reviewId}`, {
    //                 headers: { 'Authorization': `Bearer ${token}` }
    //             });
    //             fetchReviews(token);
    //         } else if (action === 'edit') {
    //             // 여기에 리뷰 수정 로직을 구현하세요
    //             console.log(`Edit review ${reviewId}`);
    //         }
    //     } catch (error) {
    //         console.error(`리뷰 ${action} 중 오류 발생:`, error);
    //     }
    // };

    if (error) {
        return (
            <div className="error-container">
                <button onClick={() => navigate('/')}>홈으로 돌아가기</button>
            </div>
        );
    }

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!movieDetail) {
        return <div>영화 정보를 불러오는 중 오류가 발생했습니다.</div>;
    }

    return (
        <div className="movie">
            <img
                src={movieDetail.posters && movieDetail.posters.length > 0 ? movieDetail.posters[0].posterUrls : ''}
                alt={`${movieDetail.movieTitle} 포스터`}
                className="movie_bg"
            />
            <div className="movie_explain">
                <div className="book_mark">
                    <button onClick={toggleFavorite}>
                        {isFavorite ? '❤️' : '🤍'}
                    </button>
                    <span> ({totalFavorites})</span>
                </div>
                <div className="explainDiv">
                    <div className="explain">
                        <ul className='explainUl'>
                            <li className="movieTitle">{movieDetail.movieTitle}</li>
                            <li className="movieGenre">
                                장르: {movieDetail.genres.map(genre => genre.genreName).join(', ')}
                            </li>
                            <li className="movieRunTime">상영 시간: {movieDetail.runTime}분</li>
                            <li className="movieOpenYear">개봉 년도: {movieDetail.openYear}</li>
                            <li className="movieRating">등급: {movieDetail.movieRating}</li>
                            <li className="movieDescription">줄거리: {movieDetail.movieDescription}</li>
                            <li className="movieDirectors">
                                감독:
                                <ul>
                                    {movieDetail.directors.map(director => (
                                        <li key={director.directorId}>
                                            {director.directorName}
                                            {director.directorPhoto && director.directorPhoto.length > 0 && (
                                                <img
                                                    src={director.directorPhoto[0].photoUrl}
                                                    alt={`${director.directorName} 사진`}
                                                    className="directorImg"
                                                />
                                            )}
                                        </li>
                                    ))}
                                </ul>
                            </li>
                            <li className="movieActors">
                                출연 배우:
                                <ul>
                                    {movieDetail.actors.map(actor => (
                                        <li key={actor.actorId}>
                                            {actor.actorName}
                                            {actor.actorPhoto && actor.actorPhoto.length > 0 && (
                                                <img
                                                    src={actor.actorPhoto[0].photoUrl}
                                                    alt={`${actor.actorName} 사진`}
                                                    className="actorImg"
                                                />
                                            )}
                                        </li>
                                    ))}
                                </ul>
                            </li>
                        </ul>
                    </div>
                    {/*{ movieTab } 1차 시도*/}
                    <MovieTab movieId={movieId} movieDetail={movieDetail}/>
                    {/*<div className="tabs">*/}
                    {/*    <button onClick={() => setActiveTab('reviews')}*/}
                    {/*            className={activeTab === 'reviews' ? 'active' : ''}>리뷰*/}
                    {/*    </button>*/}
                    {/*    <button onClick={() => setActiveTab('trailer')}*/}
                    {/*            className={activeTab === 'trailer' ? 'active' : ''}>예고편*/}
                    {/*    </button>*/}
                    {/*</div>*/}
                    {/*{activeTab === 'reviews' && (*/}
                    {/*    <div className="tabDiv">*/}
                    {/*        <h2>리뷰 ({totalReviews})</h2>*/}
                    {/*        <p>평균 평점: {averageRating.toFixed(1)}/5</p>*/}
                    {/*        <div onSubmit={handleSubmitReview}>*/}
                    {/*            <textarea*/}
                    {/*                value={newReview.content}*/}
                    {/*                onChange={(e) => setNewReview({...newReview, content: e.target.value})}*/}
                    {/*                placeholder="리뷰를 작성해주세요"*/}
                    {/*            />*/}
                    {/*            <input*/}
                    {/*                type="number"*/}
                    {/*                value={newReview.rating}*/}
                    {/*                onChange={(e) => setNewReview({...newReview, rating: parseInt(e.target.value)})}*/}
                    {/*                min="1"*/}
                    {/*                max="5"*/}
                    {/*            />*/}
                    {/*            <button type="submit">리뷰 작성</button>*/}
                    {/*        </div>*/}
                    {/*        <ul className="review_ul">*/}
                    {/*            {reviews.map((review) => (*/}
                    {/*                <li key={review.reviewId}>*/}
                    {/*                    <span className="reviewWriter">{review.memName}</span>*/}
                    {/*                    <span className="reviewContent">{review.reviewContent}</span>*/}
                    {/*                    <span className="reviewTime">review.modifiedDate && review.modifiedDate !== review.createDate*/}
                    {/*                            ? `수정됨: ${review.modifyDate}`*/}
                    {/*                            : `작성: ${review.createDate}`</span>*/}
                    {/*                    <span className="reviewStar">{review.reviewRating}/5</span>*/}
                    {/*                    {(memRole === 'ADMIN' || review.memName === localStorage.getItem('m')) && (*/}
                    {/*                        <div className="review_actions">*/}
                    {/*                            <button onClick={() => handleReviewAction(review.memName, 'edit')}>수정*/}
                    {/*                            </button>*/}
                    {/*                            <button*/}
                    {/*                                onClick={() => handleReviewAction(review.memName, 'delete')}>삭제*/}
                    {/*                            </button>*/}
                    {/*                        </div>*/}
                    {/*                    )}*/}
                    {/*                </li>*/}
                    {/*            ))}*/}
                    {/*        </ul>*/}
                    {/*    </div>*/}
                    {/*)}*/}
                    {/*{activeTab === 'trailer' && (*/}
                    {/*    <div className="trailer">*/}
                    {/*        {movieDetail.trailers && movieDetail.trailers.length > 0 ? (*/}
                    {/*            <video src={movieDetail.trailers[0].trailerUrl} controls>*/}
                    {/*                Your browser does not support the video tag.*/}
                    {/*            </video>*/}
                    {/*        ) : (*/}
                    {/*            <p>예고편이 없습니다.</p>*/}
                    {/*        )}*/}
                    {/*    </div>*/}
                    {/*)}*/}
                </div>
            </div>
            <div className="bg"></div>
            <div className="button">
                <button className="watch_movie_btn" onClick={() => navigate("/user/MoviePlay")}>
                    영화 보러 가기
                </button>
            </div>
        </div>
    );
}

export default MoviePage;