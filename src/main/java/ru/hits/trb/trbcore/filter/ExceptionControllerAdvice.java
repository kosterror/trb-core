package ru.hits.trb.trbcore.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.hits.trb.trbcore.dto.ErrorResponse;
import ru.hits.trb.trbcore.exception.BadRequestException;
import ru.hits.trb.trbcore.exception.InvalidAccountTypeException;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;
import ru.hits.trb.trbcore.exception.NotFoundException;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         Exception exception
    ) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ErrorCodes.INTERNAL_ERROR, "Internal service error"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException exception)
            throws NoResourceFoundException {
        throw exception;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HttpServletRequest request,
                                                                    MethodArgumentNotValidException exception) {
        logException(request, exception);

        var errors = new HashMap<String, String>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(ErrorCodes.VALIDATION_ERROR)
                        .message("Validation error")
                        .requestValidationMessages(errors)
                        .build()
                );
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(HttpServletRequest request,
                                                                    InvalidAccountTypeException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ErrorCodes.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request,
                                                                 NotFoundException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildResponse(ErrorCodes.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughMoneyException(HttpServletRequest request,
                                                                       NotEnoughMoneyException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ErrorCodes.NOT_ENOUGH_MONEY_FOR_ACTION, exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(HttpServletRequest request,
                                                                   BadRequestException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ErrorCodes.BAD_REQUEST, exception.getMessage()));
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

    private ErrorResponse buildResponse(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

}
