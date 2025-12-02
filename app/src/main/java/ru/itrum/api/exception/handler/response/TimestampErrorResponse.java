package ru.itrum.api.exception.handler.response;

import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.sql.Timestamp;

public record TimestampErrorResponse(
        HttpStatus status,
        Throwable cause,
        Timestamp timestamp
) implements ErrorResponse {

    @Override
    @Nonnull
    public HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    @Nonnull
    public ProblemDetail getBody() {
        return ProblemDetail.forStatus(status);
    }
}

