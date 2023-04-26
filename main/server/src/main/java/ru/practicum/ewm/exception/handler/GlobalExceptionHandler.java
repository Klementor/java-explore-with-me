package ru.practicum.ewm.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.ConflictException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ServletException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequestExceptionsHandle(final Exception ex) {
        log.error("Incorrectly made request: {}.", ex.getMessage());
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request")
                .message(ex.getMessage())
                .errors(getErrors(ex))
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError entityNotFoundExceptionHandler(final Exception ex) {
        log.error("The required object was not found: {}.", ex.getMessage());
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found")
                .message(ex.getMessage())
                .errors(getErrors(ex))
                .build();
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            ConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError dataIntegrityViolationExceptionHandler(final Exception ex) {
        log.error("Integrity constraint has been violated: {}.", ex.getMessage());
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated")
                .message(ex.getMessage())
                .errors(getErrors(ex))
                .build();
    }

    public static List<String> getErrors(Throwable throwable) {
        if (throwable.getCause() == null) {
            return null;
        }

        List<String> errors = new ArrayList<>();

        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
            errors.add(throwable.getMessage());
        }

        return errors;
    }


    @Builder
    @Getter
    public static class ApiError {
        private HttpStatus status;
        private String reason;
        private String message;

        @JsonInclude(Include.NON_NULL)
        private List<String> errors;

        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();
    }
}
