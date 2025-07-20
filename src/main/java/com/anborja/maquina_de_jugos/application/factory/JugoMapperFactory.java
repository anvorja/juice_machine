package com.anborja.maquina_de_jugos.application.factory;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import org.springframework.stereotype.Component;

@Component
public class JugoMapperFactory {

    public JugoEntity toEntity(PedidoDTO dto) {
        return new JugoEntity(
                dto.getSabor(),
                dto.isConAzucar(),
                dto.isConHielo(),
                dto.getTopping(),
                dto.getTamanoVaso(),
                dto.getClienteNombre()
        );
    }

    public JugoResponseDTO toResponseDTO(JugoEntity entity) {
        return new JugoResponseDTO(
                entity.getId(),
                entity.getSabor(),
                entity.isConAzucar(),
                entity.isConHielo(),
                entity.getTopping(),
                entity.getTamanoVaso(),
                entity.getEstado(),
                entity.getFechaPedido(),
                entity.getPrecio(),
                entity.getClienteNombre()
        );
    }

    public PedidoDTO toPedidoDTO(JugoEntity entity) {
        return new PedidoDTO(
                entity.getSabor(),
                entity.isConAzucar(),
                entity.isConHielo(),
                entity.getTopping(),
                entity.getTamanoVaso(),
                entity.getClienteNombre()
        );
    }
}