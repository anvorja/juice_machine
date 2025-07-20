package com.anborja.maquina_de_jugos.infrastructure.repository;

import com.anborja.maquina_de_jugos.application.exceptions.JugoNotFoundException;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.spi.JugoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JugoRepositoryAdapter implements JugoRepositoryPort {

    private final JpaJugoRepository jpaRepository;

    @Override
    public JugoEntity guardar(JugoEntity jugo) {
        System.out.println("💾 Guardando jugo en la nevera (base de datos)...");
        return jpaRepository.save(jugo);
    }

    @Override
    public Optional<JugoEntity> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<JugoEntity> buscarTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public List<JugoEntity> buscarPorSabor(String sabor) {
        return jpaRepository.findBySaborContainingIgnoreCase(sabor);
    }

    @Override
    public List<JugoEntity> buscarPorCliente(String clienteNombre) {
        return jpaRepository.findByClienteNombreContainingIgnoreCase(clienteNombre);
    }

    @Override
    public List<JugoEntity> buscarPorEstado(String estado) {
        return jpaRepository.findByEstado(estado);
    }

    @Override
    public void eliminar(Long id) {
        if (!jpaRepository.existsById(id)) {
            throw new JugoNotFoundException(id);
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public void actualizarEstado(Long id, String nuevoEstado) {
        if (!jpaRepository.existsById(id)) {
            throw new JugoNotFoundException(id);
        }
        jpaRepository.updateEstado(id, nuevoEstado);
    }

    @Override
    public void agregarTopping(Long id, String topping) {
        if (!jpaRepository.existsById(id)) {
            throw new JugoNotFoundException(id);
        }
        jpaRepository.addTopping(id, topping);
    }
}