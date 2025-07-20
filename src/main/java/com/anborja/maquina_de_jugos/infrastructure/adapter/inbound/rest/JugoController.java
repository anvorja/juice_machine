package com.anborja.maquina_de_jugos.infrastructure.adapter.inbound.rest;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.application.mapper.JugoMapperFactory;
import com.anborja.maquina_de_jugos.application.usecase.*;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping
    @Operation(
            summary = "🆕 Crear nuevo pedido de jugo",
            description = "Crea un nuevo pedido de jugo con las especificaciones del cliente. El sistema validará el sabor disponible y calculará automáticamente el precio.",
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Pedido creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Jugo de Mango",
                                    value = """
                                            {
                                              "id": 1,
                                              "sabor": "mango",
                                              "conAzucar": true,
                                              "conHielo": true,
                                              "topping": "granola",
                                              "tamanoVaso": "grande",
                                              "estado": "PREPARANDO",
                                              "fechaPedido": "2025-01-20T15:30:00",
                                              "precio": 6000.0,
                                              "clienteNombre": "Ana García"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "🚫 Error en la validación de datos",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Sabor no disponible",
                                            value = "🍹 El sabor 'kiwi' no está disponible en este momento"
                                    ),
                                    @ExampleObject(
                                            name = "Campo obligatorio",
                                            value = "📝 Error de validación: El sabor es obligatorio"
                                    )
                            }
                    )
            )
    })
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
    public ResponseEntity<List<JugoResponseDTO>> listarTodosLosJugos() {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutar();
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugoResponseDTO> buscarPorId(@PathVariable Long id) {
        JugoEntity jugo = buscarJugoPorIdUseCase.ejecutar(id);
        JugoResponseDTO response = mapper.toResponseDTO(jugo);
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

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<JugoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutar(estado);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tamano/{tamano}") // ✅ ENDPOINT NUEVO - Filtrar por tamaño
    public ResponseEntity<List<JugoResponseDTO>> buscarPorTamano(@PathVariable String tamano) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutarPorTamano(tamano);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reporte") // ✅ ENDPOINT NUEVO - Reporte de ventas
    public ResponseEntity<GenerarReporteVentasUseCase.ReporteVentas> generarReporte() {
        GenerarReporteVentasUseCase.ReporteVentas reporte = generarReporteVentasUseCase.ejecutar();
        return ResponseEntity.ok(reporte);
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