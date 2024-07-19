import React, { useEffect, useRef } from 'react';


// component
import RankingSlide from '../components/RankingSlide';
import RecommendReview from '../components/RecommendReview';
import RecommendStar from '../components/RecommendStar';
import UpdatedMovie from '../components/UpdatedMovie';


// css
import "../../common/css/MainPage.css";


function MainPage() {



    const initializedRef = useRef(false);

    useEffect(() => {
        if (!initializedRef.current) {
            initializedRef.current = true;
            
            // URL에서 추출
            const hash = window.location.hash.substring(1);
            const params = new URLSearchParams(hash);
            const token = params.get('token');
            const loginMethod = params.get('loginMethod');

            // 토큰 & 메서드 로컬스토리지 저장
            if (token && loginMethod) {
                localStorage.setItem('accessToken', token);
                localStorage.setItem('loginMethod', loginMethod);

                // URL 리셋
                window.history.replaceState({}, document.title, window.location.pathname + window.location.search);
                fetchMemberInfo();
            }
        }
    }, []);

    // 회원 정보 가져오는 로직
    async function fetchMemberInfo() {
        const accessToken = localStorage.getItem('accessToken');

        try {
            const response = await fetch('/auth/memberinfo', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            });

            if (!response.ok) {
                if (response.status === 401) {
                    console.error('Unauthorized access. Token might be invalid or expired.');
                    return;
                }
                throw new Error('Server responded with status: ' + response.status);
            }

            const data = await response.json();
            console.log('Member info:', data);

            // document.getElementById('member-info').innerText = JSON.stringify(data);

        } catch (error) {
            // console.error('Error fetching member info:', error);
            // document.getElementById('member-info').innerText = '오류가 발생했습니다: ' + error.message;
        }
    }

    return (

        <>

            {/* <!-- ============================ 영화 랭킹 ============================= --> */}
            <div className="ranking">
                < RankingSlide />
            </div>

            {/* <!-- ============================ 영화 랭킹 ============================= --> */}
            {/* <!-- ============================ 평점 추천 ============================= --> */}
            <div className="star_recommend">
                <div className="MainPageListHead">
                    <h2>영화 별점 추천</h2>
                </div>
                < RecommendReview />
            </div>
            {/* <!-- ============================ 평점 추천 ============================= --> */}
            {/* <!-- ============================ 랭킹(리뷰 -> 평점) ============================= --> */}
            <div className="review_recommend">
                <div className="MainPageListHead" >
                    <h2>
                        영화 리뷰 추천
                    </h2>
                </div>
                <RecommendStar />
            </div>
            {/* <!-- ============================ 랭킹(리뷰 -> 평점) ============================= --> */}
            <div className="movie_upload">
                <div className="MainPageListHead" >
                    <h2>
                        최신 업로드된 영화
                    </h2>
                </div>
                <UpdatedMovie />
            </div>







        </>






    );
}

























export default MainPage;