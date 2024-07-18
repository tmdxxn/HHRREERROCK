package com.movie.rock.movie.data.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "trailers")
public class TrailersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trailer_id")
    private Long trailerId;

    @Column(name = "trailer_url")
    private String trailerUrls;

    @Builder
    public TrailersEntity(Long trailerId, String trailerUrls) {
        this.trailerId = trailerId;
        this.trailerUrls = trailerUrls;
    }
}
