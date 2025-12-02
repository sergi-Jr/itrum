package ru.itrum.api.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.itrum.api.exception.handler.response.TimestampErrorResponse;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static java.sql.Timestamp.valueOf;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException exception) {
        return new TimestampErrorResponse(HttpStatus.NOT_FOUND, exception, valueOf(LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse handleInvalidFormatException(InvalidFormatException exception) {
        return new TimestampErrorResponse(HttpStatus.BAD_REQUEST, exception, valueOf(LocalDateTime.now()));
    }
}

