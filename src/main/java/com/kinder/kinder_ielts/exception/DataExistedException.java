package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class DataExistedException extends GlobalException{
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public DataExistedException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Data existed exception.
     *
     * @param message the message
     * @param error  the error
     */
    public DataExistedException(String message, Error error) {
        super(message, error);
    }
}
