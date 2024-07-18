package com.movie.rock.admin.service;

import com.movie.rock.admin.data.AdminMovieListRepository;
import com.movie.rock.admin.data.request.*;
import com.movie.rock.admin.data.response.*;
import com.movie.rock.common.MovieException.MovieNotFoundException;
import com.movie.rock.movie.data.entity.*;
import com.movie.rock.movie.data.repository.*;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminMovieServiceImpl implements AdminMovieService {


    private final MovieRepository movieRepository;


    private final MoviePostersRepository moviePostersRepository;

    private final TrailersRepository trailersRepository;

    private final ActorsRepository actorsRepository;

    private final DirectorsRepository directorsRepository;

    private final GenresRepository genresRepository;

    private final AdminMovieListRepository adminMovieListRepository;

    private final PostersRepository postersRepository;



    // 페이징 처리된 영화 목록을 조회합니다.
    @Override
    public Page<AdminMovieListResponseDTO> getMovieList(Pageable pageable) {
        Page<MovieEntity> moviePage = movieRepository.findAll(pageable);
        return moviePage.map(AdminMovieListResponseDTO::fromEntity);
    }

    //관리자 영화 리스트 검색(제목)
    @Override
    public Page<AdminMovieListResponseDTO> search(AdminMovieListSearchRequestDTO searchData
            , Pageable pageable) {
        Page<MovieEntity> result = null;
        if(!searchData.getMovieTitle().isEmpty()){  //영화 제목 검색
            result = adminMovieListRepository.findAllMovieTitle(searchData.getMovieTitle(),pageable);
        } else if (!searchData.getDirectorName().isEmpty()) { //영화 감독 검색
            result = adminMovieListRepository.findAllByDirectorName(searchData.getDirectorName(),pageable);
        } else if (!searchData.getMovieGenres().isEmpty()) {    //영화 장르 검색
            result = adminMovieListRepository.findAllGenres(searchData.getMovieGenres(),pageable);
        }
        List<AdminMovieListResponseDTO> adminMovieList = result.getContent().stream()
                .map(AdminMovieListResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(adminMovieList,pageable,result.getTotalElements());
    }


    //영화 상세보기
    @Override
    public AdminMovieDetailsResponseDTO getMovieDetails(Long movieId) {
        MovieEntity findMovie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        return AdminMovieDetailsResponseDTO.fromEntity(findMovie);
    }

    //영화 추가(첫번째 페이지)
    @Override
    public AdminMovieFirstInfoResponseDTO adminMovieFirstInfoResponseDTO(AdminMovieFirstInfoRequestDTO adminMovieFirstInfoRequestDTO) {
        MovieEntity movieEntity = AdminMovieFirstInfoRequestDTO.ofEntity(adminMovieFirstInfoRequestDTO);
        MovieEntity saveMovieEntity = movieRepository.save(movieEntity);
        return AdminMovieFirstInfoResponseDTO.fromEntity(saveMovieEntity);
    }

    //영화 추가(두번째 페이지)
    @Override
    public AdminMovieSecondInfoResponseDTO adminMovieSecondInfoResponseDTO(AdminMovieSecondInfoRequestDTO adminMovieSecondInfoRequestDTO) {
        MovieEntity movieEntity = AdminMovieSecondInfoRequestDTO.ofEntity(adminMovieSecondInfoRequestDTO);
        MovieEntity saveMovieEntity = movieRepository.save(movieEntity);
        return AdminMovieSecondInfoResponseDTO.fromEntity(saveMovieEntity);
    }


    //배우 추가
    @Override
    public ActorResponseDTO addActor(AdminActorRequestDTO adminActorRequestDTO) {
        ActorsEntity actorsEntity = AdminActorRequestDTO.ofEntity(adminActorRequestDTO);

        actorsEntity = actorsRepository.save(actorsEntity);
        return ActorResponseDTO.fromEntity(actorsEntity);
    }

    //감독 추가
    @Override
    public DirectorResponseDTO addDirector(AdminDirectorRequestDTO adminDirectorRequestDTO) {
        DirectorsEntity directorsEntity = AdminDirectorRequestDTO.ofEntity(adminDirectorRequestDTO); //엔티티형식으로저장

        directorsEntity = directorsRepository.save(directorsEntity);
        return DirectorResponseDTO.fromEntity(directorsEntity);//DTO형식으로 저장
    }

    //장르 추가
    @Override
    public GenreResponseDTO addGenre(AdminGenreRequestDTO adminGenreRequestDTO) {
        GenresEntity genresEntity = AdminGenreRequestDTO.ofEntity(adminGenreRequestDTO);

        genresEntity = genresRepository.save(genresEntity);
        return GenreResponseDTO.fromEntity(genresEntity);
    }

    //예고편 추가
    @Override
    public TrailerResponseDTO addTrailer(AdminTrailerRequestDTO adminTrailerRequestDTO) {
        TrailersEntity movieTrailersEntity = AdminTrailerRequestDTO.ofEntity(adminTrailerRequestDTO);

        movieTrailersEntity = trailersRepository.save(movieTrailersEntity);
        return TrailerResponseDTO.fromEntity(movieTrailersEntity);
    }

    //포스터 추가
    @Override
    public PosterResponseDTO addPoster(AdminPosterRequestDTO adminPosterRequestDTO) {
        PostersEntity moviePostersEntity = AdminPosterRequestDTO.ofEntity(adminPosterRequestDTO);

        moviePostersEntity =postersRepository.save(moviePostersEntity);
        return PosterResponseDTO.fromEntity(moviePostersEntity);
    }

    //영화 첫번째 추가 페이지수정
    @Override
    public AdminMovieFirstInfoResponseDTO updateMovieFirst(Long movieId, AdminMovieFirstInfoRequestDTO adminMovieFirstInfoRequestDTO) {
        MovieEntity existingMovie = movieRepository.findByMovieId(adminMovieFirstInfoRequestDTO.getMovieId())
                .orElseThrow(MovieNotFoundException::new);

        return AdminMovieFirstInfoResponseDTO.fromEntity(existingMovie);
    }

    //영화 두번째 추가 페이지수정
    @Override
    public AdminMovieSecondInfoResponseDTO updateMovieSecond(Long movieId, AdminMovieSecondInfoRequestDTO adminMovieSecondInfoRequestDTO) {
        MovieEntity existingMovie = movieRepository.findByMovieId(adminMovieSecondInfoRequestDTO.getMovieId())
                .orElseThrow(MovieNotFoundException::new);

        return AdminMovieSecondInfoResponseDTO.fromEntity(existingMovie);
    }


    //삭제
    @Override
    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
