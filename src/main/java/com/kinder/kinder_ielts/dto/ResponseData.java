package com.kinder.kinder_ielts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * The type Response data.
 *
 * @param <T> the type parameter
 */
@Data
public class ResponseData<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     */
// PUT, PATCH, DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     */
    // GET, POST
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param error   the error
     */
    // GET, POST
    public ResponseData(int status, String message, Error error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public static <T> ResponseData<T> ok(T data, String message) {
        return new ResponseData<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> ResponseData<T> ok(String message) {
        return new ResponseData<>(HttpStatus.OK.value(), message);
    }
}
