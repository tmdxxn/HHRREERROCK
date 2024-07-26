package com.movie.rock.admin.data.response;


import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.entity.MoviePostersEntity;
import com.movie.rock.movie.data.entity.MovieTrailersEntity;
import com.movie.rock.movie.data.response.MovieInfoResponseDTO.FilmResponseDTO;
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
    private FilmResponseDTO movieFilm;
    private List<PosterResponseDTO> poster;
    private String createDate;
    private String modifyDate;

    @Builder
    public AdminMovieSecondInfoResponseDTO(Long movieId, String movieTitle, List<TrailerResponseDTO> trailer,
                                           FilmResponseDTO movieFilm, List<PosterResponseDTO> poster,String createDate, String modifyDate) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.trailer = trailer;
        this.movieFilm = movieFilm;
        this.poster = poster;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public static AdminMovieSecondInfoResponseDTO fromEntity(MovieEntity movieEntity,
                                                             List<MovieTrailersEntity> trailer, List<MoviePostersEntity> poster) {


        return AdminMovieSecondInfoResponseDTO.builder()
                .movieId(movieEntity.getMovieId())
                .movieTitle(movieEntity.getMovieTitle())
                .trailer(movieEntity.getTrailer().stream()
                        .map(movieTrailer -> TrailerResponseDTO.builder()
                                .trailerId(movieTrailer.getTrailers().getTrailerId())
                                .trailerUrls(movieTrailer.getTrailers().getTrailerUrls())
                                .build())
                        .collect(Collectors.toList()))
                .movieFilm(FilmResponseDTO.fromEntity(movieEntity.getMovieFilm()))
                .poster(movieEntity.getPoster().stream()
                        .map(moviePoster-> PosterResponseDTO.builder()
                                .posterId(moviePoster.getPosters().getPosterId())
                                .posterUrls(moviePoster.getPosters().getPosterUrls())
                                .build())
                        .collect(Collectors.toList()))
                .createDate(movieEntity.getCreateDate())
                .modifyDate(movieEntity.getModifyDate())
                .build();
    }
}