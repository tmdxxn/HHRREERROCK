package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.DirectorsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorsRepository extends JpaRepository<DirectorsEntity, Long> {
//    List<DirectorsEntity> findByDirectorIdIn(List<Long> directorIds);
}
