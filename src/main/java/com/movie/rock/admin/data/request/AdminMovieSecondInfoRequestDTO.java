package com.movie.rock.admin.data.request;


import com.movie.rock.movie.data.entity.*;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.PosterResponseDTO;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.TrailerResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AdminMovieSecondInfoRequestDTO {
    private Long movieId;
    private String movieTitle;
    private List<TrailersEntity> trailers;
    private MovieFilmEntity movieFilm;
    private List<PostersEntity> poster;

    //생성자
    public AdminMovieSecondInfoRequestDTO(Long movieId,String movieTitle,List<TrailersEntity> trailers,MovieFilmEntity movieFilm
            ,List<PostersEntity> poster){
        this.movieId=movieId;
        this.movieTitle = movieTitle;
        this.trailers = trailers;
        this.movieFilm = movieFilm;
        this.poster = poster;
    }

    //생성자에 넣을 데이터
    @Builder
    public static MovieEntity ofEntity(AdminMovieSecondInfoRequestDTO adminMovieSecondInfoRequestDTO){
        return MovieEntity.builder()
                .movieId(adminMovieSecondInfoRequestDTO.getMovieId())
                .movieTitle(adminMovieSecondInfoRequestDTO.getMovieTitle())
                .trailer(adminMovieSecondInfoRequestDTO.getTrailers().stream()
                        .map(tr -> MovieTrailersEntity.builder()
                                .trailers(TrailersEntity.builder()
                                        .trailerId(tr.getTrailerId())
                                        .trailerUrls(tr.getTrailerUrls())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .movieFilm(adminMovieSecondInfoRequestDTO.getMovieFilm())
                .poster(adminMovieSecondInfoRequestDTO.getPoster().stream()
                        .map(posterDto -> MoviePostersEntity.builder()
                                .posters(PostersEntity.builder()
                                        .posterId(posterDto.getPosterId())
                                        .posterUrls(posterDto.getPosterUrls())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
