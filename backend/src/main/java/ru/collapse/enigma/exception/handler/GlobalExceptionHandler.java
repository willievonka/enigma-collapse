package ru.collapse.enigma.exception.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.collapse.enigma.exception.dto.ErrorResponseDto;

import java.time.Instant;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Hidden
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(Exception ex, HttpServletRequest request) {
        log.error("Internal error", ex);
        return new ErrorResponseDto(
                Instant.now(),
                "Internal Server Error",
                request.getServletPath()
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponseDto handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                "HTTP метод '%s' не поддерживается на этом эндпоинте".formatted(ex.getMethod()),
                request.getServletPath()
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                "Неверный запрос JSON",
                request.getServletPath()
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                "Неверный тип для параметра '%s': ожидается тип '%s'"
                        .formatted(ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()),
                request.getServletPath()
        );
    }

    @Hidden
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponseDto handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now(),
                "Запрашиваемый ресурс не найден",
                request.getServletPath()
        );
    }
}