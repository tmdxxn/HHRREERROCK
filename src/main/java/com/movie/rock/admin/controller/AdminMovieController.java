package com.movie.rock.admin.controller;


import com.movie.rock.admin.data.request.AdminMovieFirstInfoRequestDTO;
import com.movie.rock.admin.data.request.AdminMovieListSearchRequestDTO;
import com.movie.rock.admin.data.request.AdminMovieSecondInfoRequestDTO;
import com.movie.rock.admin.data.response.AdminMovieFirstInfoResponseDTO;
import com.movie.rock.admin.data.response.AdminMovieSecondInfoResponseDTO;
import com.movie.rock.admin.data.response.AdminMovieListResponseDTO;
import com.movie.rock.admin.service.AdminMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminMovieController {

    private final AdminMovieService movieAdminService;


    //영화 목록 페이지 렌더링
    @GetMapping("/movie/list")
    public String adminMovieList(){
        return "admin_movie_list";
    }

    //영화 추가 페이지 렌더링
    @GetMapping("/movie/list/add1")
    public String movieAddPage1(){
        return "admin_movie_upload";
    }

    //영화 추가 페이지 렌더링
    @GetMapping("/movie/list/add2")
    public String movieAddPage2(){
        return "admin_movie_upload";    //추후 페이지 생성시 주소 변경
    }

    //영화 상세(수정)페이지 랜더링
    @GetMapping("/moive/list/{movieId}/detail1")
    public String movieDetailsPage1(){
        return "admin_movie_upload";
    }

    //영화 상세(수정)페이지 랜더링
    @GetMapping("/moive/list/{movieId}/detail2")
    public String movieDetailsPage2(){
        return "admin_movie_upload";     //추후 페이지 생성시 주소 변경
    }

    //페이징 검색
    @GetMapping("/movie/list/search")
    public ResponseEntity<Page<AdminMovieListResponseDTO>> adminSearch(
            @PageableDefault(size = 10,sort = "movieId",direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String movieTitle,
            @RequestParam String movieGenres,
            @RequestParam String directorName
    ){
        AdminMovieListSearchRequestDTO searchData = AdminMovieListSearchRequestDTO.adminMovieListSearchRequestDTO
                (movieTitle,movieGenres,directorName);
        Page<AdminMovieListResponseDTO> searchList = movieAdminService.search(searchData,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(searchList);
    }


    // 영화 목록 페이지 렌더링(비동기)
    @GetMapping("/movie/movielist")
    public ResponseEntity<Page<AdminMovieListResponseDTO>> movieList(@PageableDefault(
            size = 10, sort = "movieId", direction = Sort.Direction.DESC) Pageable pageable) {
        // 페이징 처리된 영화 목록 조회
        Page<AdminMovieListResponseDTO> moviePage = movieAdminService.getMovieList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(moviePage);
    }


    //영화 추가 페이지1 정보
    @GetMapping("/movie/list/addDetail1")
    public ResponseEntity<AdminMovieFirstInfoResponseDTO> addMovie1(@RequestBody AdminMovieFirstInfoRequestDTO adminMovieFirstInfoRequestDTO){

        AdminMovieFirstInfoResponseDTO addMovieSave1 = movieAdminService.adminMovieFirstInfoResponseDTO(adminMovieFirstInfoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addMovieSave1);
    }

    //영화 추가 페이지2 정보
    @GetMapping("/movie/list/addDetail2")
    public ResponseEntity<AdminMovieSecondInfoResponseDTO> addMovie2(@RequestBody AdminMovieSecondInfoRequestDTO adminMovieSecondInfoRequestDTO){

        AdminMovieSecondInfoResponseDTO addMovieSave2 = movieAdminService.adminMovieSecondInfoResponseDTO(adminMovieSecondInfoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addMovieSave2);
    }

    //영화 상세(수정)페이지1 정보
    @PutMapping("/movie/list/{movieId}/detailList1")
    public ResponseEntity<AdminMovieFirstInfoResponseDTO> updateMovieFirst(
            @PathVariable Long movieId, @RequestBody AdminMovieFirstInfoRequestDTO requestDTO) {

        AdminMovieFirstInfoResponseDTO response = movieAdminService.updateMovieFirst(movieId,requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //영화 상세(수정)페이지2 정보
    @PutMapping("/movie/list/{movieId}/detailList2")
    public ResponseEntity<AdminMovieSecondInfoResponseDTO> updateMovieSecond(
            @PathVariable Long movieId, @RequestBody AdminMovieSecondInfoRequestDTO requestDTO
    ){
        AdminMovieSecondInfoResponseDTO response = movieAdminService.updateMovieSecond(movieId,requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 영화 삭제 처리
    @DeleteMapping("/{movieId}/delete")
    public ResponseEntity<Void> deleteMovie(@RequestBody List<Long> movieIds) {

        for (Long movieId : movieIds) {
            movieAdminService.deleteMovie(movieId);
        }
        return ResponseEntity.ok().build();
    }
}
