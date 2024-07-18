package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.MovieTrailersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieTrailersRepository extends JpaRepository<MovieTrailersEntity, Long> {
//    List<MovieTrailersEntity> findFirstByMovieMovieId(Long movieId);
}
