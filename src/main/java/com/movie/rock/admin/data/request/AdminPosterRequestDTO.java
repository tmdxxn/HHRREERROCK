package com.movie.rock.admin.data.request;


import com.movie.rock.movie.data.entity.MovieEntity;
import com.movie.rock.movie.data.entity.MoviePostersEntity;
import com.movie.rock.movie.data.entity.PostersEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPosterRequestDTO {

    private Long posterId;
    private String posterUrls; //포스터 주소

    //생성자
    private AdminPosterRequestDTO(Long posterId,String posterUrls){
        this.posterId = posterId;
        this.posterUrls = posterUrls;
    }

    //생성자에 넣을 데이터
    @Builder
    public static PostersEntity ofEntity(AdminPosterRequestDTO adminPosterRequestDTO){
        return PostersEntity.builder()
                .posterId(adminPosterRequestDTO.getPosterId())
                .posterUrls(adminPosterRequestDTO.getPosterUrls())
                .build();
    }
}
