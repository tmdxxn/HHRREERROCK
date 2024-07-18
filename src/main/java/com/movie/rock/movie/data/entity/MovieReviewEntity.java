package com.movie.rock.movie.data.entity;

import com.movie.rock.common.BaseTimeEntity;
import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.movie.data.request.MovieReviewRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reviews")
public class MovieReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_rating", nullable = false)
    private double reviewRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_num", nullable = false, updatable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private MovieEntity movie;

    @Builder
    private MovieReviewEntity(String reviewContent, double reviewRating, MemberEntity member, MovieEntity movie) {
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.member = member;
        this.movie = movie;
    }

    public void updateFromDTO(MovieReviewRequestDTO requestDTO) {
        this.reviewContent = requestDTO.getReviewContent();
        this.reviewRating = requestDTO.getReviewRating();
    }

    // equals, hashCode 메서드를 재정의
    // 객체 비교, 저장
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MovieReviewEntity that = (MovieReviewEntity) o;
//        return reviewId == that.reviewId;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(reviewId);
//    }
}
