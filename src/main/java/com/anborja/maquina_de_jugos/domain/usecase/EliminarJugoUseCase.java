package com.anborja.maquina_de_jugos.domain.usecase;

import com.anborja.maquina_de_jugos.domain.spi.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EliminarJugoUseCase {

    private final JugoRepositoryPort repository;

    public void ejecutar(Long id) {
        System.out.println("🗑️ Eliminando jugo con ID: " + id);
        repository.eliminar(id);
        System.out.println("✅ Jugo eliminado exitosamente");
    }
}