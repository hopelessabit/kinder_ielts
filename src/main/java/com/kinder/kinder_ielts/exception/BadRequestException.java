package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends GlobalException {

  /**
   * Instantiates a new Resource not found exception.
   *
   * @param message the message
   */
  public BadRequestException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Data existed exception.
   *
   * @param message the message
   * @param error  the error
   */
  public BadRequestException(String message, Error error) {
    super(message, error);
  }
}
