package com.anborja.maquina_de_jugos.application.handlers;

import com.anborja.maquina_de_jugos.application.exceptions.JugoNotFoundException;
import com.anborja.maquina_de_jugos.application.exceptions.SaborNoDisponibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JugoNotFoundException.class)
    public ResponseEntity<String> handleJugoNotFound(JugoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("🚫 " + ex.getMessage());
    }

    @ExceptionHandler(SaborNoDisponibleException.class)
    public ResponseEntity<String> handleSaborNoDisponible(SaborNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("🍹 " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        // Protección contra NullPointerException
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "Error de validación en los datos enviados";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("📝 Error de validación: " + errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("💥 Error interno del servidor: " + ex.getMessage());
    }
}