package com.anborja.maquina_de_jugos.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "jugos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JugoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sabor;

    @Column(name = "con_azucar")
    private boolean conAzucar;

    @Column(name = "con_hielo")
    private boolean conHielo;

    private String topping;

    @Column(name = "tamano_vaso")
    private String tamanoVaso;

    private String estado;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "cliente_nombre")
    private String clienteNombre;

    public JugoEntity(String sabor, boolean conAzucar, boolean conHielo,
                      String topping, String tamanoVaso, String clienteNombre) {
        this.sabor = sabor;
        this.conAzucar = conAzucar;
        this.conHielo = conHielo;
        this.topping = topping;
        this.tamanoVaso = tamanoVaso;
        this.clienteNombre = clienteNombre;
        this.estado = "PREPARANDO";
        this.fechaPedido = LocalDateTime.now();
        this.precio = calcularPrecio();
    }

    private Double calcularPrecio() {
        double precioBase = switch (tamanoVaso != null ? tamanoVaso.toLowerCase() : "mediano") {
            case "pequeño" -> 3000.0;
            case "grande" -> 5000.0;
            default -> 4000.0; // mediano
        };

        if (topping != null && !topping.isEmpty()) {
            precioBase += 1000.0;
        }

        return precioBase;
    }
}
