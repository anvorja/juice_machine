package com.anborja.maquina_de_jugos.application.usecase;

import com.anborja.maquina_de_jugos.application.exception.JugoNotFoundException;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.port.outbound.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarJugoPorIdUseCase {

    private final JugoRepositoryPort jugoRepositoryPort;

    public JugoEntity ejecutar(Long id){
        System.out.println("🔍 Buscando jugo con ID: " + id);
        return jugoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new JugoNotFoundException(id));
    }
}
