package com.kinder.kinder_ielts.exception;

import com.kinder.kinder_ielts.dto.Error;

public class SqlException extends GlobalException {
    public SqlException(String message) {
        super(message);
    }
    public SqlException(String message, Error error) {
      super(message, error);
    }
}
