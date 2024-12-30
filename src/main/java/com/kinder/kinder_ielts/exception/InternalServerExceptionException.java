package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Internal Server Exception exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InternalServerExceptionException extends GlobalException {
    public InternalServerExceptionException(String message) {
        super(message);
    }

    public InternalServerExceptionException(String message, Error error) {
        super(message, error);
    }
}
