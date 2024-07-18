package com.movie.rock.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class MemberException extends RuntimeException{
    //예외의 HTTP 상태코드
    private HttpStatus status;
    //예외의 설명메시지
    private String message;

    public MemberException (String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
