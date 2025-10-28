package com.siuuuuu.backend.exception;

public class TooManyRequestsException extends RuntimeException {
    private final Long retryAfterSeconds; // có thể null

    public TooManyRequestsException(String message) {
        super(message);
        this.retryAfterSeconds = null;
    }
    public TooManyRequestsException(String message, Long retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }
    public Long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
