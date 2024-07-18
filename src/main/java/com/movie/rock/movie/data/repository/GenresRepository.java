package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.GenresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends JpaRepository<GenresEntity,Long> {
//    Optional<GenresEntity> findByGenreName(String genreName);
}