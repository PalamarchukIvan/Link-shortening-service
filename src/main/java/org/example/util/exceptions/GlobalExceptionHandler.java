package org.example.util.exceptions;

import org.example.util.exceptions.ErrorDetails;
import org.example.util.exceptions.HashNotFoundException;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Link not found. Try using http://localhost:8080/shortening_api/create first", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceDeletedException.class)
    public ResponseEntity<?> handleResourceDeletedException(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Resource not available", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.LOCKED);
    }

    @ExceptionHandler(HashNotFoundException.class)
    public ResponseEntity<?> handleNoProtocolMentionedException(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "There is no such record with this hash", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(WebRequest request, Exception e) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
    }
}
