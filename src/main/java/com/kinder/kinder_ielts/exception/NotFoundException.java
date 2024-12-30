package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends GlobalException {

    private List<String> ids;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param error  the error
     */
    public NotFoundException(String message, Error error) {
        super(message, error);
    }
}
