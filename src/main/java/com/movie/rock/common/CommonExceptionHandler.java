package com.movie.rock.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//예외처리를 위한 컨트롤러 - API개발용(MemberException.Class)
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleMemberException(MemberException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(MovieException.class)
    public ResponseEntity<String> handleMovieException(MovieException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

//    @ExceptionHandler(UnauthorizedAccessException.class)
//    public String handleUnauthorizedAccess(UnauthorizedAccessException ue) {
//        return "redirect:/login";
//    }
}