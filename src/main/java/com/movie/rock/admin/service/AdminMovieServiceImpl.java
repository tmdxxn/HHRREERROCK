package com.movie.rock.admin.service;

import com.movie.rock.admin.data.AdminMovieListRepository;
import com.movie.rock.admin.data.request.*;
import com.movie.rock.admin.data.response.*;
import com.movie.rock.common.MovieException.MovieNotFoundException;
import com.movie.rock.movie.data.entity.*;
import com.movie.rock.movie.data.repository.*;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminMovieServiceImpl implements AdminMovieService {


    private final MovieRepository movieRepository;

    private final TrailersRepository trailersRepository;

    private final ActorsRepository actorsRepository;

    private final DirectorsRepository directorsRepository;

    private final GenresRepository genresRepository;

    private final AdminMovieListRepository adminMovieListRepository;

    private final PostersRepository postersRepository;

    private final MovieFilmRepository movieFilmRepository;

    private final MovieGenresRepository movieGenresRepository;

    private final MovieActorsRepository movieActorsRepository;

    private final MovieDirectorsRepository movieDirectorsRepository;

    private final MoviePostersRepository moviePostersRepository;

    private final MovieTrailersRepository movieTrailersRepository;


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
        if (!searchData.getMovieTitle().isEmpty()) {  //영화 제목 검색
            result = adminMovieListRepository.findAllMovieTitle(searchData.getMovieTitle(), pageable);
        } else if (!searchData.getDirectorName().isEmpty()) { //영화 감독 검색
            result = adminMovieListRepository.findAllByDirectorName(searchData.getDirectorName(), pageable);
        } else if (!searchData.getMovieGenres().isEmpty()) {    //영화 장르 검색
            result = adminMovieListRepository.findAllGenres(searchData.getMovieGenres(), pageable);
        }
        List<AdminMovieListResponseDTO> adminMovieList = result.getContent().stream()
                .map(AdminMovieListResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(adminMovieList, pageable, result.getTotalElements());
    }

    //영화 상세보기
    @Override
    public AdminMovieDetailsResponseDTO getMovieDetails(Long movieId) {
        MovieEntity findMovie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        return AdminMovieDetailsResponseDTO.fromEntity(findMovie);
    }

    //영화 추가(첫번째 페이지 이름,아이디)
    @Override
    public AdminMovieFirstInfoTitleResponseDTO saveTitleInfo(AdminMovieFirstInfoTitleRequestDTO requestDTO) {
        MovieEntity movieEntity = MovieEntity.builder()
                .movieTitle(requestDTO.getMovieTitle())
                .build();

        MovieEntity savedEntity = movieRepository.save(movieEntity);
        return AdminMovieFirstInfoTitleResponseDTO.fromEntity(savedEntity);
    }

    @Override
    public boolean existsByTitle(String title) {
        return movieRepository.existsByMovieTitle(title);
    }

    @Override
    public Page<DirectorResponseDTO> searchDirectors(String query, Pageable pageable) {
        Page<DirectorsEntity> directorsPage = directorsRepository.findByDirectorNameContaining(query, pageable);
        return directorsPage.map(DirectorResponseDTO::fromEntity);
    }

    @Override
    public Page<ActorResponseDTO> searchActors(String query, Pageable pageable) {
        Page<ActorsEntity> actorsPage = actorsRepository.findByActorNameContaining(query, pageable);
        return actorsPage.map(ActorResponseDTO::fromEntity);
    }

    @Override
    public Page<GenreResponseDTO> searchGenres(String query, Pageable pageable) {
        Page<GenresEntity> genresPage = genresRepository.findByGenreNameContaining(query, pageable);
        return genresPage.map(GenreResponseDTO::fromEntity);
    }

    //첫번째 정보
    @Override
    public AdminMovieFirstInfoResponseDTO getMovieById(Long movieId) {
        // 데이터베이스에서 영화 엔티티를 조회합니다.
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException());

        // 조회한 영화 엔티티를 DTO로 변환하여 반환합니다.
        return AdminMovieFirstInfoResponseDTO.fromEntity(movie);
    }

    @Override
    public AdminMovieSecondInfoResponseDTO getMovieByIdForSecondPage(Long movieId) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException());

        List<MovieTrailersEntity> trailers = movieTrailersRepository.findByMovie_MovieId(movieId);
        List<MoviePostersEntity> posters = moviePostersRepository.findByMovie_MovieId(movieId);

        return AdminMovieSecondInfoResponseDTO.fromEntity(movie, trailers, posters);
    }



    @Override
    @Transactional
    public AdminMovieFirstInfoResponseDTO saveFirstInfo(Long movieId, AdminMovieFirstInfoRequestDTO dto) {
        log.info("Received data for update: {}", dto);
        movieRepository.updateMovieInfo(
                movieId,
                dto.getMovieTitle(),
                dto.getRunTime(),
                dto.getOpenYear(),
                dto.getMovieRating(),
                dto.getMovieDescription()
        );

        MovieEntity updatedMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException());

        // 관계 엔티티들 업데이트
        updateGenres(updatedMovie, dto.getMovieGenres());
        updateActors(updatedMovie, dto.getMovieActors());
        updateDirectors(updatedMovie, dto.getMovieDirectors());


        return AdminMovieFirstInfoResponseDTO.fromEntity(updatedMovie);
    }



    private void updateGenres(MovieEntity movie, List<GenreResponseDTO> genreDTOs) {
        // 기존 장르 관계 삭제
        movieGenresRepository.deleteByMovieId(movie.getMovieId());

        if (genreDTOs != null) {
            for (GenreResponseDTO genreDto : genreDTOs) {
                GenresEntity genre = genresRepository.findById(genreDto.getGenreId())
                        .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreDto.getGenreId()));
                MovieGenresEntity movieGenre = new MovieGenresEntity(movie, genre);
                movieGenresRepository.save(movieGenre);
            }
        }
    }

    private void updateActors(MovieEntity movie, List<ActorResponseDTO> actorDTOs) {
        // 기존 배우 관계 삭제
        movieActorsRepository.deleteByMovieId(movie.getMovieId());

        if (actorDTOs != null) {
            for (ActorResponseDTO actorDto : actorDTOs) {
                ActorsEntity actor = actorsRepository.findById(actorDto.getActorId())
                        .orElseThrow(() -> new RuntimeException("Actor not found with id: " + actorDto.getActorId()));
                MovieActorsEntity movieActor = new MovieActorsEntity(movie, actor);
                movieActorsRepository.save(movieActor);
            }
        }
    }

    private void updateDirectors(MovieEntity movie, List<DirectorResponseDTO> directorDTOs) {
        // 기존 감독 관계 삭제
        movieDirectorsRepository.deleteByMovieId(movie.getMovieId());

        if (directorDTOs != null) {
            for (DirectorResponseDTO directorDto : directorDTOs) {
                DirectorsEntity director = directorsRepository.findById(directorDto.getDirectorId())
                        .orElseThrow(() -> new RuntimeException("Director not found with id: " + directorDto.getDirectorId()));
                MovieDirectorsEntity movieDirector = new MovieDirectorsEntity(movie, director);
                movieDirectorsRepository.save(movieDirector);
            }
        }
    }



    //영화 추가(두번째 페이지)
    @Override
    @Transactional
    public AdminMovieSecondInfoResponseDTO addCompleteMovie(AdminMovieSecondInfoRequestDTO requestDTO) {
        log.info("Adding complete movie with ID: {}", requestDTO.getMovieId());
        MovieEntity movie = movieRepository.findById(requestDTO.getMovieId())
                .orElseThrow(() -> {
                    log.error("Movie not found with id: {}. All movies: {}",
                            requestDTO.getMovieId(), movieRepository.findAll());
                    return new MovieNotFoundException();
                });
        // 영화 파일 정보 업데이트
        if (requestDTO.getMovieFilm() != null) {
            MovieFilmEntity movieFilm = MovieFilmEntity.builder()
                    .movieFilm(requestDTO.getMovieFilm().getMovieFilm())
                    .movie(movie)
                    .build();
            movieFilmRepository.save(movieFilm);
            log.info("Saved movie film: {}", movieFilm);
        }

        // 기존 트레일러 및 포스터 삭제
        movie.getTrailer().clear();
        movie.getPoster().clear();

        // 트레일러 정보 저장
        if (requestDTO.getTrailer() != null && !requestDTO.getTrailer().isEmpty()) {
            requestDTO.getTrailer().forEach(trailerDTO -> {
                TrailersEntity trailer = TrailersEntity.builder()
                        .trailerUrls(trailerDTO.getTrailerUrls())
                        .build();
                TrailersEntity savedTrailer = trailersRepository.save(trailer);
                MovieTrailersEntity movieTrailer = new MovieTrailersEntity(savedTrailer, movie);
                movie.getTrailer().add(movieTrailer);
                log.info("Saved trailer: {}", savedTrailer);
            });
        }

        // 포스터 정보 저장
        if (requestDTO.getPoster() != null && !requestDTO.getPoster().isEmpty()) {
            requestDTO.getPoster().forEach(posterDTO -> {
                PostersEntity poster = PostersEntity.builder()
                        .posterUrls(posterDTO.getPosterUrls())
                        .build();
                PostersEntity savedPoster = postersRepository.save(poster);
                MoviePostersEntity moviePoster = new MoviePostersEntity(savedPoster, movie);
                movie.getPoster().add(moviePoster);
                log.info("Saved poster: {}", savedPoster);
            });
        }

        MovieEntity savedMovie = movieRepository.save(movie);
        log.info("Saved complete movie: {}", savedMovie);

        // 엔티티를 다시 로드하여 관련된 트레일러 및 포스터 정보를 포함시킵니다.
        MovieEntity updatedMovie = movieRepository.findById(savedMovie.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException());

        List<MovieTrailersEntity> updatedTrailers = movieTrailersRepository.findByMovie_MovieId(savedMovie.getMovieId());
        List<MoviePostersEntity> updatedPosters = moviePostersRepository.findByMovie_MovieId(savedMovie.getMovieId());

        return AdminMovieSecondInfoResponseDTO.fromEntity(updatedMovie, updatedTrailers, updatedPosters);

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

        moviePostersEntity = postersRepository.save(moviePostersEntity);
        return PosterResponseDTO.fromEntity(moviePostersEntity);
    }


    @Override
    @Transactional
    public AdminMovieFirstInfoResponseDTO updateMovieFirst(Long movieId, AdminMovieFirstInfoRequestDTO dto) {
        log.info("Updating movie with ID: {}", movieId);
        log.info("DTO data: {}", dto);

        movieRepository.updateMovieInfo(
                movieId,
                dto.getMovieTitle(),
                dto.getRunTime(),
                dto.getOpenYear(),
                dto.getMovieRating(),
                dto.getMovieDescription()
        );

        // Update relationships
        MovieEntity existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException());
        updateGenres(existingMovie, dto.getMovieGenres());
        updateActors(existingMovie, dto.getMovieActors());
        updateDirectors(existingMovie, dto.getMovieDirectors());

        log.info("Movie updated: {}", existingMovie);

        return AdminMovieFirstInfoResponseDTO.fromEntity(existingMovie);
    }



    @Override
    @Transactional
    public AdminMovieSecondInfoResponseDTO updateMovieSecond(Long movieId, AdminMovieSecondInfoUpdateRequestDTO dto) {
        log.info("Updating movie with ID: {}", movieId);
        log.info("Update data: {}", dto);
        log.info("Trailer data: {}", dto.getTrailer());
        MovieEntity existingMovie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        // 영화 제목 업데이트 (기존 메서드 사용)
        movieRepository.updateMovieTitle(movieId, dto.getMovieTitle());

        // 트레일러 업데이트
        updateTrailers(existingMovie, dto.getTrailer());

        // 영화 파일 정보 업데이트
        updateMovieFilm(movieId, dto.getMovieFilm());

        // 포스터 업데이트
        updatePosters(existingMovie, dto.getPoster());

        // 변경된 엔티티 다시 조회
        MovieEntity updatedMovie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);
        // 트레일러와 포스터 리스트 가져오기
        List<MovieTrailersEntity> trailers = movieTrailersRepository.findByMovie_MovieId(movieId);
        List<MoviePostersEntity> posters = moviePostersRepository.findByMovie_MovieId(movieId);

        log.info("Updated movie data: {}", updatedMovie);
        log.info("Movie updated in database: {}", dto);
        return AdminMovieSecondInfoResponseDTO.fromEntity(updatedMovie, trailers, posters);
    }


    private void updateTrailers(MovieEntity movie, List<TrailerResponseDTO> trailerDTOs) {
        if (trailerDTOs != null && !trailerDTOs.isEmpty()) {
            movieTrailersRepository.deleteByMovie_MovieId(movie.getMovieId());

            for (TrailerResponseDTO trailerDTO : trailerDTOs) {
                TrailersEntity newTrailer = TrailersEntity.builder()
                        .trailerId(trailerDTO.getTrailerId())
                        .trailerUrls(trailerDTO.getTrailerUrls())
                        .build();
                TrailersEntity savedTrailer = trailersRepository.save(newTrailer);
                MovieTrailersEntity movieTrailer = new MovieTrailersEntity(savedTrailer, movie);
                movieTrailersRepository.save(movieTrailer);
            }
        }
    }

    private void updatePosters(MovieEntity movie, List<PosterResponseDTO> posterDTOs) {
        if (posterDTOs != null && !posterDTOs.isEmpty()) {
            // 기존 포스터 삭제
            moviePostersRepository.deleteByMovie_MovieId(movie.getMovieId());

            // 새 포스터 추가
            for (PosterResponseDTO posterDTO : posterDTOs) {
                PostersEntity newPoster = PostersEntity.builder()
                        .posterId(posterDTO.getPosterId())
                        .posterUrls(posterDTO.getPosterUrls())
                        .build();
                PostersEntity savedPoster = postersRepository.save(newPoster);
                MoviePostersEntity moviePoster = new MoviePostersEntity(savedPoster, movie);
                moviePostersRepository.save(moviePoster);
            }
        }
    }

    private void updateMovieFilm(Long movieId, FilmResponseDTO filmResponseDTO) {
        if (filmResponseDTO != null) {
            MovieFilmEntity existingMovieFilm = movieFilmRepository.findByMovie_MovieId(movieId);
            if (existingMovieFilm != null) {
                // 기존 엔티티가 있으면 업데이트
                int updatedRows = movieFilmRepository.updateMovieFilm(movieId, filmResponseDTO.getMovieFilm());
                if (updatedRows == 0) {
                    throw new RuntimeException("Failed to update movie film");
                }
            } else {
                // 기존 엔티티가 없으면 새로 생성
                int insertedRows = movieFilmRepository.insertMovieFilm(movieId, filmResponseDTO.getMovieFilm());
                if (insertedRows == 0) {
                    throw new RuntimeException("Failed to insert movie film");
                }
            }
        }
    }


    //삭제
    @Override
    @Transactional
    public void deleteMovies(List<Long> movieIds) {
        for (Long movieId : movieIds) {
            movieRepository.deleteById(movieId);
        }
    }


}
