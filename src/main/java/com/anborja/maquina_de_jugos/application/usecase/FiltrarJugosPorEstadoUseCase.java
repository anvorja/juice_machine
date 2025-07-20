package com.anborja.maquina_de_jugos.application.usecase;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.port.outbound.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FiltrarJugosPorEstadoUseCase {

    private final JugoRepositoryPort repository;

    public List<JugoEntity> ejecutar(String estado) {
        System.out.println("📊 Filtrando jugos por estado: " + estado);
        return repository.buscarPorEstado(estado);
    }

    public List<JugoEntity> ejecutarPorTamano(String tamano) {
        System.out.println("📏 Filtrando jugos por tamaño: " + tamano);
        return repository.buscarPorTamano(tamano);
    }
}
