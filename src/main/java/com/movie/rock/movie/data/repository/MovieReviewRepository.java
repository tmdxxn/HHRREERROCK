package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.MovieReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieReviewRepository extends JpaRepository<MovieReviewEntity, Long> {

    // 회원이 영화에 이미 리뷰를 작성했는지 확인
    boolean existsByMemberMemNumAndMovieMovieId(Long memNum, Long movieId);

    // 특정 영화 리뷰와 회원 정보를 포함하여 리뷰 조회
    @Query("SELECT r FROM MovieReviewEntity r JOIN FETCH r.member m JOIN FETCH r.movie b WHERE b.movieId = :movieId AND r.reviewId = :reviewId")
    Optional<MovieReviewEntity> findByIdWithMemberAndMovie(@Param("movieId") Long movieId, @Param("reviewId") Long reviewId);

    /// 작성자 본인 리뷰 가져오기
    @Query("SELECT c FROM MovieReviewEntity c JOIN FETCH c.member m JOIN FETCH c.movie b WHERE b.movieId = :movieId AND c.member.memNum = :memNum")
    Optional<MovieReviewEntity> findByMovieIdAndMemberNum(@Param("movieId") Long movieId, @Param("memNum") Long memNum);

    //시간순으로 정렬
    @Query("SELECT r FROM MovieReviewEntity r WHERE r.movie.movieId = :movieId " +
            "ORDER BY GREATEST(r.createDate, r.modifyDate) DESC")
    Page<MovieReviewEntity> findByMovieIdOrderByLatestDateDesc(
            @Param("movieId") Long movieId, Pageable pageable);

    //리뷰 전체 갯수
    Long countByMovieMovieId(Long movieId);

    //영화 평점 평균
    @Query("SELECT AVG(r.reviewRating) FROM MovieReviewEntity r WHERE r.movie.movieId = :movieId")
    Double getAverageRatingForMovie(@Param("movieId") Long movieId);
}
