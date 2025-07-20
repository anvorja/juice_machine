package com.anborja.maquina_de_jugos.domain.port.outbound;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;

import java.util.List;
import java.util.Optional;

public interface JugoRepositoryPort {
    JugoEntity guardar(JugoEntity jugo);
    Optional<JugoEntity> buscarPorId(Long id);
    List<JugoEntity> buscarTodos();
    List<JugoEntity> buscarPorSabor(String sabor);
    List<JugoEntity> buscarPorCliente(String clienteNombre);
    List<JugoEntity> buscarPorEstado(String estado);
    List<JugoEntity> buscarPorTamano(String tamano);
    void eliminar(Long id);
    void actualizarEstado(Long id, String nuevoEstado);
    void agregarTopping(Long id, String topping);
}