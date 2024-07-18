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
public class AdminMovieSecondInfoUpdateRequestDTO {
    private Long movieId;
    private String movieTitle;
    private List<TrailerResponseDTO> trailer;
    private MovieFilmEntity movieFilm;
    private List<PosterResponseDTO> poster;

    //생성자
    public AdminMovieSecondInfoUpdateRequestDTO(Long movieId, String movieTitle, List<TrailerResponseDTO> trailer, MovieFilmEntity movieFilm
            , List<PosterResponseDTO> poster){
        this.movieId=movieId;
        this.movieTitle = movieTitle;
        this.trailer = trailer;
        this.movieFilm = movieFilm;
        this.poster = poster;
    }

    //생성자에 넣을 데이터
    @Builder
    public static MovieEntity ofEntity(AdminMovieSecondInfoUpdateRequestDTO adminMovieSecondInfoUpdateRequestDTO){
        return MovieEntity.builder()
                .movieId(adminMovieSecondInfoUpdateRequestDTO.getMovieId())
                .movieTitle(adminMovieSecondInfoUpdateRequestDTO.getMovieTitle())
                .trailer(adminMovieSecondInfoUpdateRequestDTO.getTrailer().stream()
                        .map(tr->MovieTrailersEntity.builder()
                                .trailers(TrailersEntity.builder()
                                        .trailerId(tr.getTrailerId())
                                        .trailerUrls(tr.getTrailerUrls())
                                        .build())
                                .build()).collect(Collectors.toList()))
                .movieFilm(adminMovieSecondInfoUpdateRequestDTO.getMovieFilm())
                .poster(adminMovieSecondInfoUpdateRequestDTO.getPoster().stream()
                        .map(posterDto -> MoviePostersEntity.builder()
                                .posters(PostersEntity.builder()
                                        .posterId(posterDto.getPosterId())
                                        .posterUrls(posterDto.getPosterUrls())
                                        .build())
                                .build()).collect(Collectors.toList()))
                .build();
    }
}
