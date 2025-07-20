package com.anborja.maquina_de_jugos.domain.port.inbound;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;

public interface JugoServicePort {
    JugoEntity prepararJugo(String sabor, boolean conAzucar, boolean conHielo,
                            String topping, String tamanoVaso, String clienteNombre);
    void mezclarIngredientes(String sabor);
    void licuar();
    void endulzar();
    void agregarHielo();
    void decorarConTopping(String topping);
    void servirEnVaso(String tamanoVaso);
    String simularMensajeWhatsApp(String clienteNombre, String estado);
}