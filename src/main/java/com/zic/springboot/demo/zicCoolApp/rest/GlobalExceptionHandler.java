package com.zic.springboot.demo.zicCoolApp.rest;

import com.zic.springboot.demo.zicCoolApp.exceptions.UserNotFoundExeception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundExeception ex) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        userErrorResponse.setMessage(ex.getMessage());
        userErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    //Add another Exception handler to catch all exceptions
    //This handler is only by this particular rest controller.
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(Exception ex) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        userErrorResponse.setMessage(ex.getMessage());
        userErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
}
