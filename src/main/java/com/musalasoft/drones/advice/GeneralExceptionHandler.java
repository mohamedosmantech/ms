package com.musalasoft.drones.advice;



import com.musalasoft.drones.model.dto.Error;
import com.musalasoft.drones.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.musalasoft.drones.constants.ExceptionConstant.*;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException exception) {
        log.error("Validation exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(
                exception.getFieldErrors().stream().map(e -> new Error(String.format("%s, %s %s", VALIDATION_EXCEPTION_MESSAGE, e.getField(), e.getDefaultMessage())))
                        .collect(Collectors.toList())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstraintValidationException(final ConstraintViolationException exception) {
        log.error("Validation exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(List.of(new Error(String.format("%s,  %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalStateException(final IllegalStateException exception) {
        log.error("IllegalStateException exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(List.of(new Error(String.format("%s,  %s", ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, exception.getMessage()))))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(final NoSuchElementException exception) {
        log.error("NoSuchElementException exception occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(List.of(new Error(NO_SUCH_ELEMENT_EXCEPTION_MESSAGE)))
                , HttpStatus.BAD_REQUEST);
    }
}
