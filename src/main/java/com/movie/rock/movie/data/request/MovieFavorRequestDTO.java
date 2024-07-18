package com.movie.rock.movie.data.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieFavorRequestDTO {
    private Long movieId;

    public MovieFavorRequestDTO(Long movieId) {
        this.movieId = movieId;
    }
}
