package com.movie.rock.movie.service;

import com.movie.rock.movie.data.response.MainResponseDTO;

import java.util.List;

public interface MainService {
    List<MainResponseDTO> getRecentMovies();
}
