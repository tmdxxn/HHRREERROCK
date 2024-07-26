package com.movie.rock.movie.data.response;

import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.entity.MoviePostersEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MainResponseDTO {
    //포스터, 제목, 줄거리, 장르, 런타임
    //트레일러 무비아이디
    private Long movieId;
    private String movieTitle;
    private String movieDescription;
//    private String genreId;
//    private String genreName;
//    private int runtime;
    private Long posterId;
    private String posterUrls;
//    private Long trailerId;
//    private String trailerUrl;
    private String createDate;

    public MainResponseDTO(Long movieId, String movieTitle, String movieDescription, Long posterId,
                           String posterUrls, String createDate) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieDescription = movieDescription;
        this.posterId = posterId;
        this.posterUrls = posterUrls;
        this.createDate = createDate;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MainResponseDTO that = (MainResponseDTO) o;
//        return Objects.equals(movieId, that.movieId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(movieId);
//    }
}
