package com.anborja.maquina_de_jugos.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JugoResponseDTO {
    private Long id;
    private String sabor;
    private boolean conAzucar;
    private boolean conHielo;
    private String topping;
    private String tamanoVaso;
    private String estado;
    private LocalDateTime fechaPedido;
    private Double precio;
    private String clienteNombre;
}