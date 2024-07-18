package com.movie.rock.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // 이 어노테이션이 모든 필드에 대한 getter 메서드를 생성합니다.
public class MovieException extends RuntimeException {

    private final String errCode;
    private final HttpStatus status;

    public MovieException(String message, String errCode, HttpStatus status) {
        super(message);
        this.errCode = errCode;
        this.status = status;
    }

    public static class DuplicateReviewException extends MovieException {
        public DuplicateReviewException() {
            super("이미 리뷰를 작성했습니다.", "ERR_DUPLICATE_REVIEW", HttpStatus.BAD_REQUEST);
        }
    }

    public static class MovieNotFoundException extends MovieException {
        public MovieNotFoundException() {
            super("영화가 존재하지 않습니다.", "ERR_MOVIE_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

    public static class ReviewNotFoundException extends MovieException {
        public ReviewNotFoundException() {
            super("리뷰를 찾을 수 없습니다.", "ERR_REVIEW_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

    public static class UnauthorizedAccessException extends MovieException {
        public UnauthorizedAccessException() { super("접근권한이 없습니다.", "ERR_UNAUTHORIZED", HttpStatus.FORBIDDEN); }
    }

    public static class MemberNotFoundException extends MovieException {
        public MemberNotFoundException() { super("회원을 찾을 수 없습니다.", "ERR_MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND); }
    }

    public static class FavoritesNotFoundException extends MovieException {
        public FavoritesNotFoundException() { super("찜한 영화를 찾을 수 없습니다.", "ERR_FAVORITES_NOT_FOUND", HttpStatus.NOT_FOUND); }
    }

    public static class DuplicateActorException extends MovieException {
        public DuplicateActorException() { super("동일한 배우가 있습니다.", "ERR_DUPLICATE_ACTOR", HttpStatus.BAD_REQUEST); }
    }

    public static class InvalidActorPhotoTypeException extends MovieException {
        public InvalidActorPhotoTypeException() { super("배우 사진 타입이 아닙니다.", "ERR_INVALID_ACTOR_PHOTO_TYPE", HttpStatus.BAD_REQUEST); }
    }

    public static class InvalidDirectorPhotoTypeException extends MovieException {
        public InvalidDirectorPhotoTypeException() { super("감독 사진 타입이 아닙니다.", "ERR_INVALID_DIRECTOR_PHOTO_TYPE", HttpStatus.BAD_REQUEST); }
    }

    public static class PostersByMovieNotFoundException extends MovieException {
        public PostersByMovieNotFoundException() { super("해당 영화의 포스터를 찾을 수 없습니다.", "ERR_POSTERS_NOT_FOUND", HttpStatus.NOT_FOUND); }
    }
}