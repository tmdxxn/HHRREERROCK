package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.response.MainResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MainRepository extends JpaRepository<MovieEntity, Long> {

    //30일 내에 추가된 영화 있을 시,
    //포스터 중복 제거
    @Query("SELECT new com.movie.rock.movie.data.response.MainResponseDTO(m.movieId, m.movieTitle, m.movieDescription, " +
            "MIN(p.posters.posterId), MIN(p.posters.posterUrls), m.createDate) " +
            "FROM MovieEntity m " +
            "LEFT JOIN m.poster p " +
            "WHERE m.createDate > :thirtyDaysAgo " +
            "GROUP BY m.movieId, m.movieTitle, m.movieDescription, m.createDate " +
            "ORDER BY m.createDate DESC")
    List<MainResponseDTO> findRecentMoviesWithin30days(@Param("thirtyDaysAgo") String thirtyDaysAgo); //baseTimeEntity String 타입이라 변경

    //30일 내에 추가된 영화 없을 시, movieId 상위 5개
    @Query("SELECT new com.movie.rock.movie.data.response.MainResponseDTO(m.movieId, m.movieTitle, m.movieDescription, " +
            "MIN(p.posters.posterId), MIN(p.posters.posterUrls), m.createDate) " +
            "FROM MovieEntity m " +
            "LEFT JOIN m.poster p " +
            "GROUP BY m.movieId, m.movieTitle, m.movieDescription, m.createDate " +
            "ORDER BY m.movieId DESC")
    List<MainResponseDTO> findTop5ByOrderByMovieIdDesc(Pageable pageable);
}
