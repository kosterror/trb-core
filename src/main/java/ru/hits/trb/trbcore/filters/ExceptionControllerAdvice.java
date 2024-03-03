package ru.hits.trb.trbcore.filters;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hits.trb.trbcore.dto.ErrorResponse;
import ru.hits.trb.trbcore.exception.InvalidAccountTypeException;
import ru.hits.trb.trbcore.exception.NotEnoughMoney;
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
                .body(ErrorResponse.Builder()
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

    @ExceptionHandler(NotEnoughMoney.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request,
                                                                 NotEnoughMoney exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ErrorCodes.NOT_ENOUGH_MONEY_FOR_ACTION, exception.getMessage()));
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

    private ErrorResponse buildResponse(int code, String message) {
        return ErrorResponse.Builder()
                .code(code)
                .message(message)
                .build();
    }

}
