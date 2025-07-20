package com.anborja.maquina_de_jugos.domain.usecase;

import com.anborja.maquina_de_jugos.domain.api.JugoServicePort;
import com.anborja.maquina_de_jugos.domain.spi.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActualizarJugoUseCase {

    private final JugoRepositoryPort repository;
    private final JugoServicePort jugoService;

    public void cambiarEstado(Long id, String nuevoEstado) {
        System.out.println("🔄 Cambiando estado del jugo " + id + " a: " + nuevoEstado);
        repository.actualizarEstado(id, nuevoEstado);

        // Simular notificación
        String mensaje = jugoService.simularMensajeWhatsApp("Cliente", nuevoEstado);
        System.out.println("📱 " + mensaje);
    }

    public void agregarToppingExtra(Long id, String topping) {
        System.out.println("✨ Agregando topping extra: " + topping + " al jugo " + id);
        repository.agregarTopping(id, topping);
    }
}