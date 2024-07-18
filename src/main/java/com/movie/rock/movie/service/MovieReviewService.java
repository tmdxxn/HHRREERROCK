package com.movie.rock.movie.service;

import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.movie.data.request.MovieReviewRequestDTO;
import com.movie.rock.movie.data.response.MovieReviewPageResponseDTO;
import com.movie.rock.movie.data.response.MovieReviewResponseDTO;

public interface MovieReviewService {

    MovieReviewPageResponseDTO getMovieReviews(Long movieId, int page, MemberEntity member);

    MovieReviewResponseDTO createMovieReview(Long movieId, MovieReviewRequestDTO movieReviewRequestDTO, MemberEntity member);

    MovieReviewResponseDTO updateMovieReview(Long movieId, Long reviewId, MovieReviewRequestDTO requestDTO, MemberEntity member);

    void deleteMovieReview(Long movieId, Long reviewId, MemberEntity member);

    Long getTotalReviews(Long movieId);

    double getAverageRating(Long movieId);
}
