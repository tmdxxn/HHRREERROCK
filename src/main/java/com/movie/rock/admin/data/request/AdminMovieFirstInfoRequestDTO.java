package com.movie.rock.admin.data.request;


import com.movie.rock.movie.data.entity.*;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AdminMovieFirstInfoRequestDTO {
    private Long movieId;
    private String movieTitle;
    private List<GenreResponseDTO> movieGenres;
    private int runTime;
    private Integer openYear;
    private String movieRating;
    private String movieDescription;
    private List<ActorResponseDTO> movieActors;
    //배우사진 추가
    private List<DirectorResponseDTO> movieDirectors;
    //감독사진 추가

    //생성자
    public AdminMovieFirstInfoRequestDTO(Long movieId,String movieTitle,List<GenreResponseDTO> movieGenres
            ,int runTime,Integer openYear,String movieRating,String movieDescription,List<ActorResponseDTO> movieActors,List<DirectorResponseDTO> movieDirectors){
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieGenres = movieGenres;
        this.runTime = runTime;
        this.openYear = openYear;
        this.movieRating = movieRating;
        this.movieDescription = movieDescription;
        this.movieActors = movieActors;
        this.movieDirectors = movieDirectors;
    }

    //생성자에 넣을 데이터
    @Builder
    public static MovieEntity ofEntity(AdminMovieFirstInfoRequestDTO adminMovieFirstInfoRequestDTO){
        return MovieEntity.builder()
                .movieId(adminMovieFirstInfoRequestDTO.getMovieId())
                .movieTitle(adminMovieFirstInfoRequestDTO.getMovieTitle())
                .genres(adminMovieFirstInfoRequestDTO.getMovieGenres().stream()
                        .map(genreDto -> MovieGenresEntity.builder()
                                .genre(GenresEntity.builder()
                                        .genreId(genreDto.getGenreId())
                                        .genreName(genreDto.getGenreName())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .runTime(adminMovieFirstInfoRequestDTO.getRunTime())
                .openYear(adminMovieFirstInfoRequestDTO.getOpenYear())
                .movieRating(adminMovieFirstInfoRequestDTO.getMovieRating())
                .movieDescription(adminMovieFirstInfoRequestDTO.getMovieDescription())
                .movieActors(adminMovieFirstInfoRequestDTO.getMovieActors().stream()
                        .map(movieActorDto -> MovieActorsEntity.builder()
                                .actor(ActorsEntity.builder()
                                        .actorId(movieActorDto.getActorId())
                                        .actorName(movieActorDto.getActorName())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .movieDirectors(adminMovieFirstInfoRequestDTO.getMovieDirectors().stream()
                        .map(movieDirectorDto -> MovieDirectorsEntity.builder()
                                .director(DirectorsEntity.builder()
                                        .directorId(movieDirectorDto.getDirectorId())
                                        .directorName(movieDirectorDto.getDirectorName())
                                        .build())
                                .build())
                        .collect(Collectors.toList())
                )
                .build();

    }
}
