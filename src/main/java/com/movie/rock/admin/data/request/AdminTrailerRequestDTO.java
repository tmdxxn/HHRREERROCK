package com.movie.rock.admin.data.request;


import com.movie.rock.movie.data.entity.TrailersEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminTrailerRequestDTO {

    private Long trailerId;
    private String trailerUrls; //트레일러 URL

    //생성자
    private AdminTrailerRequestDTO(Long trailerId,String trailerUrls){
        this.trailerId = trailerId;
        this.trailerUrls = trailerUrls;
    }

    //생성자에 넣을 데이터
    @Builder
    public static TrailersEntity ofEntity(AdminTrailerRequestDTO adminTrailerRequestDTO){
        return TrailersEntity.builder()
                .trailerId(adminTrailerRequestDTO.getTrailerId())
                .trailerUrls(adminTrailerRequestDTO.getTrailerUrls())
                .build();
    }
}
