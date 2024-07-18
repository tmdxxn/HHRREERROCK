package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.MovieGenresEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenresRepository extends JpaRepository<MovieGenresEntity, Long> {
//    List<MovieGenresEntity> findByMovieMovieId(Long movieId);

    // 기존 장르 관계를 모두 제거합니다. ->MovieServiceImpl
//    void deleteByMovie(MovieEntity movie);
}
