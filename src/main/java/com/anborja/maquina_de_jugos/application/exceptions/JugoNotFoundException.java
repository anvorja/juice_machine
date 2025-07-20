package com.anborja.maquina_de_jugos.application.exceptions;

public class JugoNotFoundException extends RuntimeException {
    public JugoNotFoundException(String message) {
        super(message);
    }

    public JugoNotFoundException(Long id) {
        super("Jugo no encontrado con ID: " + id);
    }
}