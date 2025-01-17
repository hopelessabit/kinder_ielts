package com.kinder.kinder_ielts.util;

import com.kinder.kinder_ielts.dto.Error;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.exception.*;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.function.Supplier;

public class ResponseUtil {
    public final static String SQL_ERROR_MESSAGE = "Lỗi database";
    public static  <T> ResponseEntity<ResponseData<T>> getResponse(T response, String message){
        try {
            return new ResponseEntity<>(ResponseData.ok(response, message), HttpStatus.OK);
        } catch (BadRequestException | DataExistedException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getError()), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), e.getError()), HttpStatus.NOT_FOUND);
        } catch (InternalServerExceptionException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getError()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessResourceFailureException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> ResponseEntity<ResponseData<T>> getResponse(Supplier<T> responseSupplier, String message) {
        try {
            T response = responseSupplier.get(); // Call the function
            return new ResponseEntity<>(ResponseData.ok(response, message), HttpStatus.OK);
        } catch (BadRequestException | DataExistedException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getError()), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), e.getError()), HttpStatus.NOT_FOUND);
        } catch (InternalServerExceptionException  e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getError()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SqlException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), SQL_ERROR_MESSAGE, e.getError()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessResourceFailureException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<ResponseData<Void>> getResponse(Runnable action, String message) {
        try {
            action.run(); // Execute the action
            return new ResponseEntity<>(ResponseData.ok(null, message), HttpStatus.OK);
        } catch (BadRequestException | DataExistedException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getError()), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), e.getError()), HttpStatus.NOT_FOUND);
        } catch (InternalServerExceptionException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getError()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SqlException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), SQL_ERROR_MESSAGE, e.getError()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessResourceFailureException e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), Error.build(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static  <T> ResponseEntity<ResponseData<T>> getResponse(ResponseData<T> response) {
        if (response.getStatus() == HttpStatus.OK.value())
            return ResponseEntity.ok(response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }
}
