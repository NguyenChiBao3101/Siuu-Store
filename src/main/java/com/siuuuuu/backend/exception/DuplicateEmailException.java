package com.siuuuuu.backend.exception;

import javax.management.RuntimeMBeanException;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
