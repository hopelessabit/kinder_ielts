package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GlobalException extends RuntimeException {
    private final Error error;

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public GlobalException(String message) {
        super(message);
        this.error = null;
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param error   the error
     */
    public GlobalException(String message, Error error) {
        super(message);
        this.error = error;
    }
}