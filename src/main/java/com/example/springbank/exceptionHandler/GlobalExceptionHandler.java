package com.example.springbank.exceptionHandler;

import com.example.springbank.exceptionHandler.dto.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ResponseErrorDto> handleClientErrors(Exception ex) {

        log.warn("CLIENT ERROR: {}", ex.getMessage());

        ResponseErrorDto error = ResponseErrorDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDto> handleServerErrors(Exception ex) {

        log.error("SERVER ERROR", ex);

        ResponseErrorDto error = ResponseErrorDto.builder()
                .message("Internal server error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
