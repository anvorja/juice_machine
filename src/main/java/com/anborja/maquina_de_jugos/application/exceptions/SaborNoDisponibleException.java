package com.anborja.maquina_de_jugos.application.exceptions;

public class SaborNoDisponibleException extends RuntimeException {
    public SaborNoDisponibleException(String sabor) {
        super("El sabor '" + sabor + "' no está disponible en este momento");
    }
}