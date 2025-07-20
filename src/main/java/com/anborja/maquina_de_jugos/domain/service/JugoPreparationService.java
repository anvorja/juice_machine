package com.anborja.maquina_de_jugos.domain.service;

import com.anborja.maquina_de_jugos.application.exception.SaborNoDisponibleException;
import com.anborja.maquina_de_jugos.domain.port.inbound.JugoServicePort;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;

import java.util.Arrays;
import java.util.List;

public class JugoPreparationService implements JugoServicePort {

    private final List<String> saboresDisponibles = Arrays.asList(
            "mango", "fresa", "naranja", "piña", "manzana", "pera", "maracuyá",
            "guayaba", "lulo", "mora", "banano", "papaya"
    );

    @Override
    public JugoEntity prepararJugo(String sabor, boolean conAzucar, boolean conHielo,
                                   String topping, String tamanoVaso, String clienteNombre) {

        System.out.println("🍹 === INICIANDO PREPARACIÓN DE JUGO ===");

        // Validar sabor disponible
        if (!saboresDisponibles.contains(sabor.toLowerCase())) {
            throw new SaborNoDisponibleException(sabor);
        }

        // Secuencia de preparación
        mezclarIngredientes(sabor);
        licuar();

        if (conAzucar) {
            endulzar();
        }

        if (conHielo) {
            agregarHielo();
        }

        servirEnVaso(tamanoVaso);

        if (topping != null && !topping.isEmpty()) {
            decorarConTopping(topping);
        }

        System.out.println("✅ ¡Jugo de " + sabor + " listo para " + clienteNombre + "!");

        return new JugoEntity(sabor, conAzucar, conHielo, topping, tamanoVaso, clienteNombre);
    }

    @Override
    public void mezclarIngredientes(String sabor) {
        System.out.println("🥭 Mezclando " + sabor + " fresco...");
        simularTiempo(1000);
    }

    @Override
    public void licuar() {
        System.out.println("🌀 Licuando a máxima velocidad...");
        simularTiempo(2000);
    }

    @Override
    public void endulzar() {
        System.out.println("🍯 Agregando azúcar al gusto...");
        simularTiempo(500);
    }

    @Override
    public void agregarHielo() {
        System.out.println("🧊 Añadiendo cubitos de hielo...");
        simularTiempo(800);
    }

    @Override
    public void decorarConTopping(String topping) {
        System.out.println("✨ Decorando con " + topping + "...");
        simularTiempo(700);
    }

    @Override
    public void servirEnVaso(String tamanoVaso) {
        System.out.println("🥤 Sirviendo en vaso " + tamanoVaso + "...");
        simularTiempo(500);
    }

    @Override
    public String simularMensajeWhatsApp(String clienteNombre, String estado) {
        return switch (estado.toUpperCase()) {
            case "PREPARANDO" -> "📱 Hola " + clienteNombre + "! Tu jugo está siendo preparado 🍹";
            case "LISTO" -> "📱 ¡" + clienteNombre + ", tu jugo está listo! 🎉 Puedes venir a recogerlo";
            case "ENTREGADO" -> "📱 Gracias " + clienteNombre + " por tu compra! 😊 ¡Que disfrutes tu jugo!";
            default -> "📱 Hola " + clienteNombre + ", estado de tu pedido: " + estado;
        };
    }

    private void simularTiempo(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}