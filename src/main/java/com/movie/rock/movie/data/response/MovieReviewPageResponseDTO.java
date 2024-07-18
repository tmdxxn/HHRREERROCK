package com.movie.rock.movie.data.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MovieReviewPageResponseDTO {
    private List<MovieReviewResponseDTO> reviews;
    private MovieReviewResponseDTO ownReview;
    private int currentPage;
    private int startPage;
    private int endPage;
    private int totalPages;
    private boolean hasPrevious;
    private boolean hasNext;
    private long totalReviews;
    private double averageRating;
}