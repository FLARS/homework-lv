package io.fourfinanceit.integration.rest;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleException(ValidationException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleException(EntityNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, (e.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handleException(ApplicationException e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private class ApiError {
        private HttpStatus status;
        private String message;
        private LocalDateTime timestamp;

        private ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
            timestamp = LocalDateTime.now();
        }
    }
}
