package com.licencias.licencias.exception;

import com.licencias.licencias.dto.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("BusinessException: {} - {}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(build(ex.getMessage(), ex.getErrorCode(), request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();
        return ResponseEntity.badRequest().body(build("Error de validación", "VALIDATION_ERROR", request.getRequestURI(), details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldErrorDetail> details = ex.getConstraintViolations().stream()
                .map(v -> ErrorResponse.FieldErrorDetail.builder()
                        .field(v.getPropertyPath().toString())
                        .message(v.getMessage())
                        .rejectedValue(v.getInvalidValue())
                        .build())
                .toList();
        return ResponseEntity.badRequest().body(build("Error de validación", "VALIDATION_ERROR", request.getRequestURI(), details));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(build("Credenciales inválidas", "UNAUTHORIZED", request.getRequestURI(), null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(build("Acceso denegado", "FORBIDDEN", request.getRequestURI(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}", request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(build("Error interno del servidor", "INTERNAL_ERROR", request.getRequestURI(), null));
    }

    private ErrorResponse.FieldErrorDetail toFieldError(FieldError error) {
        return ErrorResponse.FieldErrorDetail.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue())
                .build();
    }

    private ErrorResponse build(String message, String code, String path, List<ErrorResponse.FieldErrorDetail> errors) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(code)
                .errors(errors)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }
}
