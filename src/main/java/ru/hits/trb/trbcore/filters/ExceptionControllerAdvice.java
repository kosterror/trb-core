package ru.hits.trb.trbcore.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hits.trb.trbcore.dto.ErrorResponse;
import ru.hits.trb.trbcore.exception.ApplicationException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         Exception exception
    ) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.Builder()
                        .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Internal server error")
                        .build()
                );
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    ApplicationException exception) {
        logException(request, exception);

        var error = exception.getError();

        return ResponseEntity
                .status(error.httpStatus)
                .body(ErrorResponse.Builder()
                        .httpCode(error.httpStatus.value())
                        .code(error.code)
                        .message(error.httpStatus.is5xxServerError() ?
                                "Internal server error" :
                                exception.getMessage()
                        ).build()
                );
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

}
