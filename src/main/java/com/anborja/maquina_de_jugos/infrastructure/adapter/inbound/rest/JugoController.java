package com.anborja.maquina_de_jugos.infrastructure.adapter.inbound.rest;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.application.mapper.JugoMapperFactory;
import com.anborja.maquina_de_jugos.application.usecase.*;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import com.anborja.maquina_de_jugos.infrastructure.documentation.ApiDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "🍹 Gestión de Jugos", description = "API para gestionar pedidos de jugos naturales")
public class JugoController {

    private final CrearJugoUseCase crearJugoUseCase;
    private final ListarJugosUseCase listarJugosUseCase;
    private final BuscarJugoPorIdUseCase buscarJugoPorIdUseCase;
    private final FiltrarJugosPorEstadoUseCase filtrarJugosPorEstadoUseCase;
    private final GenerarReporteVentasUseCase generarReporteVentasUseCase;
    private final ActualizarJugoUseCase actualizarJugoUseCase;
    private final EliminarJugoUseCase eliminarJugoUseCase;
    private final JugoMapperFactory mapper;

    // ================== ENDPOINTS PRINCIPALES ==================

    @PostMapping
    @ApiDocumentation.CrearJugoDocumentation
    public ResponseEntity<JugoResponseDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        System.out.println("📱 NUEVO PEDIDO POR WHATSAPP: " + pedidoDTO.getClienteNombre() +
                " quiere un jugo de " + pedidoDTO.getSabor());

        // 1. Validar que el pedido esté completo
        // 2. Pasar al Chef Principal
        JugoEntity jugo = crearJugoUseCase.ejecutar(pedidoDTO);

        // 3. Traducir respuesta para el cliente
        JugoResponseDTO response = mapper.toResponseDTO(jugo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ApiDocumentation.ListarJugosDocumentation
    public ResponseEntity<List<JugoResponseDTO>> listarTodosLosJugos() {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutar();
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ApiDocumentation.BuscarJugoPorIdDocumentation
    public ResponseEntity<JugoResponseDTO> buscarPorId(@PathVariable Long id) {
        JugoEntity jugo = buscarJugoPorIdUseCase.ejecutar(id);
        JugoResponseDTO response = mapper.toResponseDTO(jugo);
        return ResponseEntity.ok(response);
    }

    // ================== ENDPOINTS DE BÚSQUEDA ==================

    @GetMapping("/sabor/{sabor}")
    @ApiDocumentation.BuscarJugosPorSaborDocumentation
    public ResponseEntity<List<JugoResponseDTO>> buscarPorSabor(@PathVariable String sabor) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorSabor(sabor);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteNombre}")
    @ApiDocumentation.BuscarJugosPorClienteDocumentation
    public ResponseEntity<List<JugoResponseDTO>> buscarPorCliente(@PathVariable String clienteNombre) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorCliente(clienteNombre);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @ApiDocumentation.BuscarJugosPorEstadoDocumentation
    public ResponseEntity<List<JugoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutar(estado);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tamano/{tamano}")
    @ApiDocumentation.BuscarJugosPorTamanoDocumentation
    public ResponseEntity<List<JugoResponseDTO>> buscarPorTamano(@PathVariable String tamano) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutarPorTamano(tamano);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ================== ENDPOINTS DE REPORTES ==================

    @GetMapping("/reporte")
    @ApiDocumentation.GenerarReporteDocumentation
    public ResponseEntity<GenerarReporteVentasUseCase.ReporteVentas> generarReporte() {
        GenerarReporteVentasUseCase.ReporteVentas reporte = generarReporteVentasUseCase.ejecutar();
        return ResponseEntity.ok(reporte);
    }

    // ================== ENDPOINTS DE ACTUALIZACIÓN ==================

    @PatchMapping("/{id}/estado")
    @ApiDocumentation.CambiarEstadoDocumentation
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id,
                                                @RequestParam String estado) {
        actualizarJugoUseCase.cambiarEstado(id, estado);
        return ResponseEntity.ok("✅ Estado actualizado a: " + estado);
    }

    @PatchMapping("/{id}/topping")
    @ApiDocumentation.AgregarToppingDocumentation
    public ResponseEntity<String> agregarTopping(@PathVariable Long id,
                                                 @RequestParam String topping) {
        actualizarJugoUseCase.agregarToppingExtra(id, topping);
        return ResponseEntity.ok("✨ Topping agregado: " + topping);
    }

    // ================== ENDPOINTS DE ELIMINACIÓN ==================

    @DeleteMapping("/{id}")
    @ApiDocumentation.EliminarJugoDocumentation
    public ResponseEntity<String> eliminarJugo(@PathVariable Long id) {
        eliminarJugoUseCase.ejecutar(id);
        return ResponseEntity.ok("🗑️ Jugo eliminado exitosamente");
    }
}