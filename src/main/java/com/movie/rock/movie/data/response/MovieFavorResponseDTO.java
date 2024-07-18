    package com.movie.rock.movie.data.response;

    import com.movie.rock.movie.data.entity.MovieEntity;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class MovieFavorResponseDTO {
        private Long movieId;
        private String movieTitle;
        private Long posterId;
        private boolean isFavorite;
        private Long favorCount;
        private String memName;

        public MovieFavorResponseDTO(Long movieId, String movieTitle, Long posterId,
                                     boolean isFavorite, Long favorCount, String memName) {
            this.movieId = movieId;
            this.movieTitle = movieTitle;
            this.posterId = posterId;
            this.isFavorite = isFavorite;
            this.favorCount = favorCount;
            this.memName = memName;
        }

        public static MovieFavorResponseDTO fromEntity(MovieEntity movie, Long posterId,
                                                       boolean isFavorite, Long favorCount, String memName) {
            return new MovieFavorResponseDTO(
                    movie.getMovieId(),
                    movie.getMovieTitle(),
                    posterId, // 대체값 생각하기 , 미정
                    false,
                    favorCount,
                    memName
            );
        }
    }