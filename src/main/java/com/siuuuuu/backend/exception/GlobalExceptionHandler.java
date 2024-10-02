package com.siuuuuu.backend.exception;

import com.siuuuuu.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse<?>> handleAppException(AppException e) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(e.getStatusCode(), e.getMessage(), null);
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse<?> errorResponse = new ErrorResponse<>(400, "Bad request", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ErrorResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(HttpStatus.FORBIDDEN.value(), "Access to this resource is forbidden", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
