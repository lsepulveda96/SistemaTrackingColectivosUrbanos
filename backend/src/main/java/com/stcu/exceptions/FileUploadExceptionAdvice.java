package com.stcu.exceptions;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException( MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
            .body("File too large!!!");
    }
}
