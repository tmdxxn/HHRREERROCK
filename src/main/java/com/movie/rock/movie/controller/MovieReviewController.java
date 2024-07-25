package com.movie.rock.movie.controller;

import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.member.data.MemberRepository;
import com.movie.rock.member.service.CustomUserDetails;
import com.movie.rock.movie.data.request.MovieReviewRequestDTO;
import com.movie.rock.movie.data.response.MovieReviewPageResponseDTO;
import com.movie.rock.movie.data.response.MovieReviewResponseDTO;
import com.movie.rock.movie.service.MovieReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/movies/detail")
@RequiredArgsConstructor
public class MovieReviewController {

    private final MovieReviewService movieReviewService;
    private final MemberRepository memberRepository;

    @GetMapping("/{movieId}/reviews")
    public ResponseEntity<MovieReviewPageResponseDTO> getMovieReviews(
            @PathVariable("movieId") Long movieId,
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity member = userDetails.memberEntity();

        MovieReviewPageResponseDTO reviewPage = movieReviewService.getMovieReviews(movieId, page, member);
        return ResponseEntity.ok(reviewPage);
    }

    @PostMapping("/{movieId}/reviews")
    public ResponseEntity<MovieReviewResponseDTO> createMovieReview(
            @PathVariable("movieId") Long movieId,
            @Valid @RequestBody MovieReviewRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity member = userDetails.memberEntity();

        MovieReviewResponseDTO review = movieReviewService.createMovieReview(movieId, requestDTO, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<MovieReviewResponseDTO> updateMovieReview(
            @PathVariable("movieId") Long movieId,
            @PathVariable("reviewId") Long reviewId,
            @Valid @RequestBody MovieReviewRequestDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity member = userDetails.memberEntity();

        MovieReviewResponseDTO review = movieReviewService.updateMovieReview(movieId, reviewId, requestDTO, member);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{movieId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteMovieReview(
            @PathVariable("movieId") Long movieId,
            @PathVariable("reviewId") Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity member = userDetails.memberEntity();

        movieReviewService.deleteMovieReview(movieId, reviewId, member);
        return ResponseEntity.noContent().build();
    }
}

