package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizeException extends GlobalException{

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public UnauthorizeException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param error  the error
     */
    public UnauthorizeException(String message, Error error) {
        super(message, error);
    }
}

