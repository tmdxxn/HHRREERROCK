package com.movie.rock.admin.data.response;


import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.entity.MovieFilmEntity;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.GenreResponseDTO;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.PosterResponseDTO;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.TrailerResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AdminMovieSecondInfoResponseDTO {
    private Long movieId;
    private String movieTitle;
    private List<TrailerResponseDTO> trailer;
    private MovieFilmEntity movieFilm;
    private List<PosterResponseDTO> poster;


    //생성자
    @Builder
    public AdminMovieSecondInfoResponseDTO(Long movieId,String movieTitle,List<TrailerResponseDTO> trailer
            ,MovieFilmEntity movieFilm, List<PosterResponseDTO> poster){
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.trailer = trailer;
        this.movieFilm = movieFilm;
        this.poster = poster;
    }

    //생성자에 넣을 데이터
    public static AdminMovieSecondInfoResponseDTO fromEntity(MovieEntity movieEntity){
        return AdminMovieSecondInfoResponseDTO.builder()
                .movieId(movieEntity.getMovieId())
                .movieTitle(movieEntity.getMovieTitle())
                .trailer(movieEntity.getTrailer().stream()
                        .map(movieTrailer -> TrailerResponseDTO.builder()
                                .trailerId(movieTrailer.getTrailers().getTrailerId())
                                .trailerUrls(movieTrailer.getTrailers().getTrailerUrls())
                                .build())
                        .collect(Collectors.toList()))
                .movieFilm(movieEntity.getMovieFilm())
                .poster(movieEntity.getPoster().stream()
                        .map(moviePoster-> PosterResponseDTO.builder()
                                .posterId(moviePoster.getPosters().getPosterId())
                                .posterUrls(moviePoster.getPosters().getPosterUrls())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
