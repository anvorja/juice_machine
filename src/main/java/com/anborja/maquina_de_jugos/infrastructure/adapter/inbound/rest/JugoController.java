package com.anborja.maquina_de_jugos.infrastructure.adapter.inbound.rest;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.dto.PedidoDTO;
import com.anborja.maquina_de_jugos.application.mapper.JugoMapperFactory;
import com.anborja.maquina_de_jugos.application.usecase.*;
import com.anborja.maquina_de_jugos.domain.model.JugoEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<JugoResponseDTO> crearPedido(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del pedido de jugo",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "Jugo básico",
                                            value = """
                                                    {
                                                      "sabor": "fresa",
                                                      "conAzucar": true,
                                                      "conHielo": false,
                                                      "tamanoVaso": "mediano",
                                                      "clienteNombre": "Carlos López"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Jugo premium",
                                            value = """
                                                    {
                                                      "sabor": "maracuyá",
                                                      "conAzucar": false,
                                                      "conHielo": true,
                                                      "topping": "chía y granola",
                                                      "tamanoVaso": "grande",
                                                      "clienteNombre": "María Rodríguez"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) PedidoDTO pedidoDTO) {

        System.out.println("📱 NUEVO PEDIDO POR WHATSAPP: " + pedidoDTO.getClienteNombre() +
                " quiere un jugo de " + pedidoDTO.getSabor());

        JugoEntity jugo = crearJugoUseCase.ejecutar(pedidoDTO);
        JugoResponseDTO response = mapper.toResponseDTO(jugo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "📋 Listar todos los jugos",
            description = "Obtiene la lista completa de todos los jugos registrados en el sistema",
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de jugos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = JugoResponseDTO.class))
    )
    public ResponseEntity<List<JugoResponseDTO>> listarTodosLosJugos() {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutar();
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "🔍 Buscar jugo por ID",
            description = "Obtiene los detalles de un jugo específico mediante su identificador único",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugo encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "🚫 Jugo no encontrado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(value = "🚫 Jugo no encontrado con ID: 999")
                    )
            )
    })
    public ResponseEntity<JugoResponseDTO> buscarPorId(
            @Parameter(description = "ID único del jugo", example = "1")
            @PathVariable Long id) {
        JugoEntity jugo = buscarJugoPorIdUseCase.ejecutar(id);
        JugoResponseDTO response = mapper.toResponseDTO(jugo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sabor/{sabor}")
    @Operation(
            summary = "🥭 Buscar jugos por sabor",
            description = "Filtra jugos por sabor específico (búsqueda no sensible a mayúsculas)",
            tags = {"🔍 Búsquedas"}
    )
    public ResponseEntity<List<JugoResponseDTO>> buscarPorSabor(
            @Parameter(
                    description = "Sabor del jugo a buscar",
                    example = "mango",
                    schema = @Schema(allowableValues = {
                            "mango", "fresa", "naranja", "piña", "manzana",
                            "pera", "maracuyá", "guayaba", "lulo", "mora",
                            "banano", "papaya"
                    })
            )
            @PathVariable String sabor) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorSabor(sabor);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteNombre}")
    @Operation(
            summary = "👤 Buscar jugos por cliente",
            description = "Obtiene todos los pedidos realizados por un cliente específico",
            tags = {"🔍 Búsquedas"}
    )
    public ResponseEntity<List<JugoResponseDTO>> buscarPorCliente(
            @Parameter(description = "Nombre del cliente", example = "Ana García")
            @PathVariable String clienteNombre) {
        List<JugoEntity> jugos = listarJugosUseCase.ejecutarPorCliente(clienteNombre);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "📊 Buscar jugos por estado",
            description = "Filtra jugos según su estado actual de preparación",
            tags = {"🔍 Búsquedas"}
    )
    public ResponseEntity<List<JugoResponseDTO>> buscarPorEstado(
            @Parameter(
                    description = "Estado del pedido",
                    example = "LISTO",
                    schema = @Schema(allowableValues = {"PREPARANDO", "LISTO", "ENTREGADO"})
            )
            @PathVariable String estado) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutar(estado);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tamano/{tamano}")
    @Operation(
            summary = "🥤 Buscar jugos por tamaño",
            description = "Filtra jugos según el tamaño del vaso",
            tags = {"🔍 Búsquedas"}
    )
    public ResponseEntity<List<JugoResponseDTO>> buscarPorTamano(
            @Parameter(
                    description = "Tamaño del vaso",
                    example = "grande",
                    schema = @Schema(allowableValues = {"pequeño", "mediano", "grande"})
            )
            @PathVariable String tamano) {
        List<JugoEntity> jugos = filtrarJugosPorEstadoUseCase.ejecutarPorTamano(tamano);
        List<JugoResponseDTO> response = jugos.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reporte")
    @Operation(
            summary = "📈 Generar reporte de ventas",
            description = "Genera un reporte completo con estadísticas de ventas, sabores más vendidos e ingresos",
            tags = {"📊 Reportes"}
    )
    @ApiResponse(
            responseCode = "200",
            description = "Reporte generado exitosamente",
            content = @Content(
                    schema = @Schema(implementation = GenerarReporteVentasUseCase.ReporteVentas.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "fecha": "2025-01-20",
                                      "jugosPreparando": 3,
                                      "jugosListos": 5,
                                      "jugosEntregados": 12,
                                      "saboresMasVendidos": {
                                        "mango": 4,
                                        "fresa": 3,
                                        "naranja": 2
                                      },
                                      "tamanosMasVendidos": {
                                        "grande": 6,
                                        "mediano": 4,
                                        "pequeño": 2
                                      },
                                      "ingresosTotales": 54000.0
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<GenerarReporteVentasUseCase.ReporteVentas> generarReporte() {
        GenerarReporteVentasUseCase.ReporteVentas reporte = generarReporteVentasUseCase.ejecutar();
        return ResponseEntity.ok(reporte);
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "🔄 Cambiar estado del jugo",
            description = "Actualiza el estado de un jugo específico (ej: de PREPARANDO a LISTO)",
            tags = {"🍹 Gestión de Jugos"}
    )
    public ResponseEntity<String> cambiarEstado(
            @Parameter(description = "ID del jugo") @PathVariable Long id,
            @Parameter(description = "Nuevo estado", schema = @Schema(allowableValues = {"PREPARANDO", "LISTO", "ENTREGADO"}))
            @RequestParam String estado) {
        actualizarJugoUseCase.cambiarEstado(id, estado);
        return ResponseEntity.ok("✅ Estado actualizado a: " + estado);
    }

    @PatchMapping("/{id}/topping")
    @Operation(
            summary = "✨ Agregar topping extra",
            description = "Añade un topping adicional a un jugo existente",
            tags = {"🍹 Gestión de Jugos"}
    )
    public ResponseEntity<String> agregarTopping(
            @Parameter(description = "ID del jugo") @PathVariable Long id,
            @Parameter(description = "Topping a agregar", example = "granola")
            @RequestParam String topping) {
        actualizarJugoUseCase.agregarToppingExtra(id, topping);
        return ResponseEntity.ok("✨ Topping agregado: " + topping);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "🗑️ Eliminar jugo",
            description = "Elimina un jugo del sistema (útil para cancelar pedidos)",
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Jugo no encontrado")
    })
    public ResponseEntity<String> eliminarJugo(
            @Parameter(description = "ID del jugo a eliminar") @PathVariable Long id) {
        eliminarJugoUseCase.ejecutar(id);
        return ResponseEntity.ok("🗑️ Jugo eliminado exitosamente");
    }
}