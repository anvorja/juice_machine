package com.anborja.maquina_de_jugos.domain.usecase;

import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.domain.api.JugoServicePort;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.spi.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearJugoUseCase {

    private final JugoServicePort jugoService;
    private final JugoRepositoryPort repository;

    public JugoEntity ejecutar(PedidoDTO pedido) {
        System.out.println("🎯 INICIANDO PEDIDO PARA: " + pedido.getClienteNombre());

        // Paso 1: Preparar el jugo usando el servicio
        JugoEntity jugo = jugoService.prepararJugo(
                pedido.getSabor(),
                pedido.isConAzucar(),
                pedido.isConHielo(),
                pedido.getTopping(),
                pedido.getTamanoVaso(),
                pedido.getClienteNombre()
        );

        // Paso 2: Guardar en la "nevera" (base de datos)
        JugoEntity jugoGuardado = repository.guardar(jugo);

        // Paso 3: Simular mensaje de WhatsApp
        String mensaje = jugoService.simularMensajeWhatsApp(
                pedido.getClienteNombre(),
                "LISTO"
        );
        System.out.println("📱 " + mensaje);

        return jugoGuardado;
    }
}