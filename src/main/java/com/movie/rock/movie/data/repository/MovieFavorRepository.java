package com.movie.rock.movie.data.repository;

import com.movie.rock.movie.data.entity.MovieFavorEntity;
import com.movie.rock.movie.data.pk.MovieFavorPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieFavorRepository extends JpaRepository<MovieFavorEntity, MovieFavorPK> {

    //영화 Id에 대한 찜 엔티티 조회
//    List<MovieFavorEntity> findByMovieMovieId(Long movieId);

    //찜 총 갯수
    Long countByMovieMovieId(Long movieId);

    //찜 존재여부
    boolean existsByMemberMemNumAndMovieMovieId(Long memNum, Long movieId);

    //찜 제거
    void deleteByMemberMemNumAndMovieMovieId(Long memNum, Long movieId);

    //회원 찜 리스트
    List<MovieFavorEntity> findByMemberMemNum(Long memNum);

//    Optional<MovieFavorEntity> findByMovieMovieIdAndMemberMemNum(Long movieId, Long memNum);
}
