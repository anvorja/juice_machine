package com.anborja.maquina_de_jugos.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @NotBlank(message = "El sabor es obligatorio")
    private String sabor;

    private boolean conAzucar;
    private boolean conHielo;
    private String topping;

    @NotBlank(message = "El tamaño del vaso es obligatorio")
    private String tamanoVaso; // pequeño, mediano, grande

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;
}