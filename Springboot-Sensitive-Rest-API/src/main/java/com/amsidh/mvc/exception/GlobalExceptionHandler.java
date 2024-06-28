package com.amsidh.mvc.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public ErrorDetail handleRuntimeException(RuntimeException runtimeException) {
        return ErrorDetail.builder().code("RUNTIME").message(runtimeException.getMessage()).timestamp(Instant.now()).build();
    }

    @ExceptionHandler(value = {Exception.class})
    public ErrorDetail handleException(Exception exception) {
        return ErrorDetail.builder().code("EXCEPTION").message(exception.getMessage()).timestamp(Instant.now()).build();
    }

}