package ru.collapse.enigma.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.collapse.enigma.exception.EntityNotFoundException;
import ru.collapse.enigma.exception.dto.ErrorResponseDto;

import java.time.Instant;

@RestControllerAdvice
public class ProjectExceptionHandler {

    @Hidden
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                ex.getMessage(),
                request.getServletPath()
        );
    }
}
