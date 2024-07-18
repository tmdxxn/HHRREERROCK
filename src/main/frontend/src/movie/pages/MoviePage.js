import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

import '../../common/css/MoviePage.css';

function MoviePage() {
    const [movieDetail, setMovieDetail] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const { movieId } = useParams();

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                alert("로그인이 필요합니다.");
                navigate('/login');
                return;
            }
            try {
                await fetchMovieDetail(token);
                setIsLoading(false);
            } catch (error) {
                console.error("데이터 로딩 중 오류 발생:", error);
                setIsLoading(false);
            }
        };
        fetchData();
    }, [movieId, navigate]);

    const fetchMovieDetail = async (token) => {
        try {
            const response = await axios.get(`/user/detail/${movieId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            setMovieDetail(response.data);
            console.log('영화 상세 정보:', response.data);
        } catch (error) {
            console.error('영화 상세 정보를 가져오는 중 오류 발생:', error);
        }
    };

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
                </div>
            </div>
        </div>
    );
}

export default MoviePage;
