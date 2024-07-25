package com.movie.rock.movie.controller;

import com.movie.rock.movie.data.response.MainResponseDTO;
import com.movie.rock.movie.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/recent")
    public ResponseEntity<List<MainResponseDTO>> getRecentMovies() {
        List<MainResponseDTO> recentMovies = mainService.getRecentMovies();

        return ResponseEntity.ok(recentMovies);
    }
}
