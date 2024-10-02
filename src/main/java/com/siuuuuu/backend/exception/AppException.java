package com.siuuuuu.backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppException extends RuntimeException{
    private int statusCode;

    public AppException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
