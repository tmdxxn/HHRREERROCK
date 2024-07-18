package com.movie.rock.movie.data.response;

import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.movie.data.entity.MovieReviewEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class  MovieReviewResponseDTO {

    private Long reviewId;
    private String reviewContent;
    private double reviewRating;
    private Long memNum;
    private String memName;
    private String createDate;
    private String modifyDate;

    @Builder
    public MovieReviewResponseDTO(Long reviewId, String reviewContent, double reviewRating,
                                  Long memNum, String memName, String createDate, String modifyDate) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.reviewRating = reviewRating;
        this.memNum = memNum;
        this.memName = memName;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public static MovieReviewResponseDTO fromEntity(MovieReviewEntity review, MemberEntity member) {
        return MovieReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .reviewRating(review.getReviewRating())
                .memNum(member.getMemNum())
                .memName(member.getMemName())
                .createDate(review.getCreateDate())
                .modifyDate(review.getModifyDate())
                .build();
    }
}
