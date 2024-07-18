package com.movie.rock.movie.service;

import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.member.data.MemberRepository;
import com.movie.rock.member.data.RoleEnum;
import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.entity.MovieReviewEntity;
import com.movie.rock.movie.data.repository.MovieRepository;
import com.movie.rock.movie.data.repository.MovieReviewRepository;
import com.movie.rock.movie.data.request.MovieReviewRequestDTO;
import com.movie.rock.movie.data.response.MovieReviewPageResponseDTO;
import com.movie.rock.movie.data.response.MovieReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.movie.rock.common.MovieException.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewServiceImpl implements MovieReviewService {

    private final MovieRepository movieRepository;
    private final MovieReviewRepository movieReviewRepository;
    private final MemberRepository memberRepository;

    //전체 리뷰 갯수
    @Transactional(readOnly = true)
    public Long getTotalReviews(Long movieId) {
        return movieReviewRepository.countByMovieMovieId(movieId);
    }

    //영화별 리뷰 평균
    @Transactional(readOnly = true)
    public double getAverageRating(Long movieId) {
        Double averageRating = movieReviewRepository.getAverageRatingForMovie(movieId);
        return averageRating != null ? averageRating : 0;
    }

    //리뷰 페이징처리
    @Override
    @Transactional(readOnly = true)
    public MovieReviewPageResponseDTO getMovieReviews(Long movieId, int page, MemberEntity member) {
        //페이지 1부터 시작
        if (page < 1) {
            page = 1;
        }

        //페이지 10개씩, 생성시간을 기준으로 내림차순
        Pageable pageable = PageRequest.of(page - 1, 10);

        //영화리뷰목록 가져오기
        Page<MovieReviewEntity> reviewPage = movieReviewRepository.findByMovieIdOrderByLatestDateDesc(movieId, pageable);

        // 본인 리뷰 찾기
        Optional<MovieReviewEntity> ownReviewOptional = movieReviewRepository.findByMovieIdAndMemberNum(movieId, member.getMemNum());

        // 본인 리뷰가 있고 현재 페이지가 1페이지인 경우
        List<MovieReviewResponseDTO> reviewDTOs;

        if (ownReviewOptional.isPresent() && page == 1) {
            MovieReviewEntity ownReview = ownReviewOptional.get();
            reviewDTOs = new ArrayList<>();
            reviewDTOs.add(MovieReviewResponseDTO.fromEntity(ownReview, ownReview.getMember()));

            // 나머지 리뷰 추가 (본인 리뷰 제외)
            reviewDTOs.addAll(reviewPage.getContent().stream()
                    .filter(review -> !review.getReviewId().equals(ownReview.getReviewId()))
                    .map(review -> MovieReviewResponseDTO.fromEntity(review, review.getMember()))
                    .collect(Collectors.toList()));
        } else {
            // 본인 리뷰가 없거나 1페이지가 아닌 경우 모든 리뷰 추가
            reviewDTOs = reviewPage.getContent().stream()
                    .map(review -> MovieReviewResponseDTO.fromEntity(review, review.getMember()))
                    .collect(Collectors.toList());
        }

        //총 리뷰수 계산 (본인 리뷰 있다면  +1)
        long totalElements = reviewPage.getTotalElements() + (ownReviewOptional.isPresent() ? 1 : 0);

        //페이징
        int totalPages = (int) ((reviewPage.getTotalElements() + 9) / 10);
        int currentPage = page;
        int startPage = Math.max(currentPage - 4, 1);
        int endPage = Math.min(currentPage + 5, totalPages);

        return MovieReviewPageResponseDTO.builder()
                .reviews(reviewDTOs)
                .currentPage(currentPage)
                .startPage(startPage)
                .endPage(endPage)
                .totalPages(totalPages)
                .hasPrevious(currentPage > 1)
                .hasNext(currentPage < totalPages)
                .ownReview(ownReviewOptional.map(review -> MovieReviewResponseDTO.fromEntity(review, member)).orElse(null))
                .totalReviews(getTotalReviews(movieId))
                .averageRating(getAverageRating(movieId))
                .build();
    }

    //리뷰작성
    @Override
    public MovieReviewResponseDTO createMovieReview(Long movieId, MovieReviewRequestDTO requestDTO, MemberEntity member) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        if (movieReviewRepository.existsByMemberMemNumAndMovieMovieId(member.getMemNum(), movieId)) {
            throw new DuplicateReviewException();
        }

        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);

        MovieReviewEntity review = MovieReviewEntity.builder()
                .reviewContent(requestDTO.getReviewContent())
                .reviewRating(requestDTO.getReviewRating())
                .member(member)
                .movie(movie)
                .build();

        MovieReviewEntity savedReview = movieReviewRepository.save(review);
        return MovieReviewResponseDTO.fromEntity(savedReview, member);
    }

    //리뷰수정
    @Override
    public MovieReviewResponseDTO updateMovieReview(Long movieId, Long reviewId, MovieReviewRequestDTO requestDTO, MemberEntity member) {
        MovieReviewEntity existingReview = movieReviewRepository.findByIdWithMemberAndMovie(movieId, reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        if (!existingReview.getMember().getMemNum().equals(member.getMemNum())) {
            throw new UnauthorizedAccessException();
        }

        existingReview.updateFromDTO(requestDTO);

        MovieReviewEntity savedReview = movieReviewRepository.save(existingReview);
        return MovieReviewResponseDTO.fromEntity(savedReview, member);
    }

    //리뷰삭제
    @Override
    public void deleteMovieReview(Long movieId, Long reviewId, MemberEntity member) {
        //관리자 삭제권한
        if (member.getMemRole() == RoleEnum.ADMIN) {
            if (!movieReviewRepository.existsById(reviewId)) {
                throw new ReviewNotFoundException();
            }
            movieReviewRepository.deleteById(reviewId);
        } else {
            //회원삭제
            MovieReviewEntity review = movieReviewRepository.findByIdWithMemberAndMovie(movieId, reviewId)
                    .orElseThrow(ReviewNotFoundException::new);

            if (!review.getMember().getMemNum().equals(member.getMemNum())) {
                throw new UnauthorizedAccessException();
            }

            movieReviewRepository.delete(review);
        }
    }
}
