package com.anborja.maquina_de_jugos.domain.usecase;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.spi.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarJugosUseCase {

    private final JugoRepositoryPort repository;

    public List<JugoEntity> ejecutar() {
        return repository.buscarTodos();
    }

    public List<JugoEntity> ejecutarPorSabor(String sabor) {
        return repository.buscarPorSabor(sabor);
    }

    public List<JugoEntity> ejecutarPorCliente(String clienteNombre) {
        return repository.buscarPorCliente(clienteNombre);
    }
}