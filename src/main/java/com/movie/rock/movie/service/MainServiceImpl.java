package com.movie.rock.movie.service;

import com.movie.rock.movie.data.repository.MainRepository;
import com.movie.rock.movie.data.response.MainResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final MainRepository mainRepository;

    @Override
    public List<MainResponseDTO> getRecentMovies() {

        //30일 내 추가된 영화 있을 시,
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        String thirtyDaysAgoString = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //baseTimeEntity 타입이 String이라 변경

       List<MainResponseDTO> recentMovies = mainRepository.findRecentMoviesWithin30days(thirtyDaysAgoString);
        log.debug("Recent movies within 30 days: {}", recentMovies.size());

        //30일 내 추가된 영화 없을 시, movieId 상위 5개 값 출력
        if (recentMovies.isEmpty()) {
            Pageable topFive = PageRequest.of(0, 5);
            recentMovies = mainRepository.findTop5ByOrderByMovieIdDesc(topFive);
            log.debug("Top 5 recent movies: {}", recentMovies.size());
        }

        //포스터 중복 제거
        Map<Long, MainResponseDTO> uniqueMovies = new LinkedHashMap<>();
        for (MainResponseDTO movie : recentMovies) {
            uniqueMovies.putIfAbsent(movie.getMovieId(), movie);
        }

        return new ArrayList<>(uniqueMovies.values());
    }
}
