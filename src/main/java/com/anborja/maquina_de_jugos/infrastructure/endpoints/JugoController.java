package com.anborja.maquina_de_jugos.infrastructure.endpoints;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.application.factory.JugoMapperFactory;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.domain.usecase.ActualizarJugoUseCase;
import com.anborja.maquina_de_jugos.domain.usecase.CrearJugoUseCase;
import com.anborja.maquina_de_jugos.domain.usecase.EliminarJugoUseCase;
import com.anborja.maquina_de_jugos.domain.usecase.ListarJugosUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jugos")
@RequiredArgsConstructor
public class JugoController {

    private final CrearJugoUseCase crearJugoUseCase;
    private final ListarJugosUseCase listarJugosUseCase;
    private final ActualizarJugoUseCase actualizarJugoUseCase;
    private final EliminarJugoUseCase eliminarJugoUseCase;
    private final JugoMapperFactory mapper;

    @PostMapping
    public ResponseEntity<JugoResponseDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        System.out.println("📱 NUEVO PEDIDO POR WHATSAPP: " + pedidoDTO.getClienteNombre() +
                " quiere un jugo de " + pedidoDTO.getSabor());

        JugoEntity jugo = crearJugoUseCase.ejecutar(pedidoDTO);
        JugoResponseDTO response = mapper.toResponseDTO(jugo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<JugoResponseDTO>> listarTodosLosJugos() {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutar();
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sabor/{sabor}")
    public ResponseEntity<List<JugoResponseDTO>> buscarPorSabor(@PathVariable String sabor) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorSabor(sabor);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteNombre}")
    public ResponseEntity<List<JugoResponseDTO>> buscarPorCliente(@PathVariable String clienteNombre) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorCliente(clienteNombre);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id,
                                                @RequestParam String estado) {
        actualizarJugoUseCase.cambiarEstado(id, estado);
        return ResponseEntity.ok("✅ Estado actualizado a: " + estado);
    }

    @PatchMapping("/{id}/topping")
    public ResponseEntity<String> agregarTopping(@PathVariable Long id,
                                                 @RequestParam String topping) {
        actualizarJugoUseCase.agregarToppingExtra(id, topping);
        return ResponseEntity.ok("✨ Topping agregado: " + topping);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarJugo(@PathVariable Long id) {
        eliminarJugoUseCase.ejecutar(id);
        return ResponseEntity.ok("🗑️ Jugo eliminado exitosamente");
    }
}