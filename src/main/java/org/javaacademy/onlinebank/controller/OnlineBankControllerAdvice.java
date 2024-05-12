package org.javaacademy.onlinebank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OnlineBankControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("У нас произошла ошибка, уже работаем над ней");
    }
}
