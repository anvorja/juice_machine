package com.anborja.maquina_de_jugos.infrastructure.adapter.outbound.persistence;

import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface JpaJugoRepository extends JpaRepository<JugoEntity, Long> {

    List<JugoEntity> findBySaborContainingIgnoreCase(String sabor);

    List<JugoEntity> findByClienteNombreContainingIgnoreCase(String clienteNombre);

    List<JugoEntity> findByEstado(String estado);

    @Query("SELECT j FROM JugoEntity j WHERE j.tamanoVaso = :tamano")
    List<JugoEntity> findByTamanoVaso(@Param("tamano") String tamano);

    @Modifying
    @Transactional
    @Query("UPDATE JugoEntity j SET j.estado = :estado WHERE j.id = :id")
    void updateEstado(@Param("id") Long id, @Param("estado") String estado);

    @Modifying
    @Transactional
    @Query("UPDATE JugoEntity j SET j.topping = CONCAT(COALESCE(j.topping, ''), ', ', :topping) WHERE j.id = :id")
    void addTopping(@Param("id") Long id, @Param("topping") String topping);
}