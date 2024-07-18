package com.movie.rock.movie.data.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovieReviewRequestDTO {

    @NotBlank
    private String reviewContent;

    @Min(1)
    @Max(5)
    private double reviewRating;
}
