package com.anborja.maquina_de_jugos.infrastructure.documentation;

import com.anborja.maquina_de_jugos.application.dto.JugoResponseDTO;
import com.anborja.maquina_de_jugos.application.usecase.GenerarReporteVentasUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ApiDocumentation {

    // ========== ANOTACIONES PARA ENDPOINTS PRINCIPALES ==========

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🆕 Crear nuevo pedido de jugo",
            description = """
                    Crea un nuevo pedido de jugo con las especificaciones del cliente.
                    El sistema validará que el sabor esté disponible y calculará automáticamente
                    el precio según el tamaño del vaso y los toppings agregados.
                    """,
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
                                    name = "Jugo de Mango Premium",
                                    value = """
                                            {
                                              "id": 1,
                                              "sabor": "mango",
                                              "conAzucar": true,
                                              "conHielo": true,
                                              "topping": "granola",
                                              "tamanoVaso": "grande",
                                              "estado": "PREPARANDO",
                                              "fechaPedido": "2025-07-20T15:30:00",
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
                                    ),
                                    @ExampleObject(
                                            name = "Tamaño inválido",
                                            value = "📝 Error de validación: El tamaño del vaso es obligatorio"
                                    )
                            }
                    )
            )
    })
    public @interface CrearJugoDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "📋 Listar todos los jugos",
            description = "Obtiene la lista completa de todos los jugos registrados en el sistema, sin importar su estado actual.",
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Lista de jugos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Lista de jugos",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "sabor": "mango",
                                                "conAzucar": true,
                                                "conHielo": true,
                                                "topping": "granola",
                                                "tamanoVaso": "grande",
                                                "estado": "LISTO",
                                                "fechaPedido": "2025-07-20T15:30:00",
                                                "precio": 6000.0,
                                                "clienteNombre": "Ana García"
                                              },
                                              {
                                                "id": 2,
                                                "sabor": "fresa",
                                                "conAzucar": false,
                                                "conHielo": true,
                                                "topping": null,
                                                "tamanoVaso": "mediano",
                                                "estado": "PREPARANDO",
                                                "fechaPedido": "2025-07-20T16:00:00",
                                                "precio": 4000.0,
                                                "clienteNombre": "Carlos López"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "📭 No hay jugos registrados en el sistema"
            )
    })
    public @interface ListarJugosDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🔍 Buscar jugo por ID",
            description = "Obtiene los detalles completos de un jugo específico mediante su identificador único.",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugo encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "🚫 Jugo no encontrado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Jugo no encontrado",
                                    value = "🚫 Jugo no encontrado con ID: 999"
                            )
                    )
            )
    })
    public @interface BuscarJugoPorIdDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🥭 Buscar jugos por sabor",
            description = "Filtra y obtiene todos los jugos que coincidan con el sabor especificado. La búsqueda no es sensible a mayúsculas y minúsculas.",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugos encontrados por sabor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Jugos de mango",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "sabor": "mango",
                                                "conAzucar": true,
                                                "conHielo": true,
                                                "topping": "granola",
                                                "tamanoVaso": "grande",
                                                "estado": "ENTREGADO",
                                                "fechaPedido": "2025-07-20T14:30:00",
                                                "precio": 6000.0,
                                                "clienteNombre": "María Rodríguez"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "📭 No se encontraron jugos con ese sabor"
            )
    })
    public @interface BuscarJugosPorSaborDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "👤 Buscar jugos por cliente",
            description = "Obtiene todos los pedidos realizados por un cliente específico. Útil para ver el historial de compras.",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugos del cliente encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "📭 El cliente no tiene pedidos registrados"
            )
    })
    public @interface BuscarJugosPorClienteDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "📊 Buscar jugos por estado",
            description = "Filtra jugos según su estado actual de preparación. Útil para gestionar el flujo de trabajo de la máquina.",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugos filtrados por estado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Jugos listos para entregar",
                                    value = """
                                            [
                                              {
                                                "id": 5,
                                                "sabor": "piña",
                                                "conAzucar": false,
                                                "conHielo": true,
                                                "topping": "chía",
                                                "tamanoVaso": "mediano",
                                                "estado": "LISTO",
                                                "fechaPedido": "2025-07-20T16:45:00",
                                                "precio": 5000.0,
                                                "clienteNombre": "Roberto Silva"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "📭 No hay jugos en ese estado"
            )
    })
    public @interface BuscarJugosPorEstadoDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🥤 Buscar jugos por tamaño de vaso",
            description = "Filtra jugos según el tamaño del vaso utilizado. Útil para análisis de ventas y preferencias de los clientes.",
            tags = {"🔍 Búsquedas"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugos filtrados por tamaño",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JugoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "📭 No hay jugos con ese tamaño de vaso"
            )
    })
    public @interface BuscarJugosPorTamanoDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "📈 Generar reporte de ventas",
            description = """
                    Genera un reporte completo con estadísticas de ventas del día actual, incluyendo:
                    - Cantidad de jugos por estado
                    - Sabores más vendidos
                    - Tamaños más populares
                    - Ingresos totales generados
                    """,
            tags = {"📊 Reportes"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Reporte generado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenerarReporteVentasUseCase.ReporteVentas.class),
                            examples = @ExampleObject(
                                    name = "Reporte diario",
                                    value = """
                                            {
                                              "fecha": "2025-07-20",
                                              "jugosPreparando": 3,
                                              "jugosListos": 5,
                                              "jugosEntregados": 12,
                                              "saboresMasVendidos": {
                                                "mango": 4,
                                                "fresa": 3,
                                                "naranja": 2,
                                                "piña": 2,
                                                "maracuyá": 1
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
    })
    public @interface GenerarReporteDocumentation {}

    // ========== ANOTACIONES PARA OPERACIONES DE ACTUALIZACIÓN ==========

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🔄 Cambiar estado del jugo",
            description = """
                    Actualiza el estado de un jugo específico en el flujo de preparación:
                    - PREPARANDO → LISTO → ENTREGADO
                    
                    También envía una notificación simulada por WhatsApp al cliente.
                    """,
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Estado actualizado exitosamente",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Estado actualizado",
                                    value = "✅ Estado actualizado a: LISTO"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "🚫 Jugo no encontrado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Jugo no encontrado",
                                    value = "🚫 Jugo no encontrado con ID: 999"
                            )
                    )
            )
    })
    public @interface CambiarEstadoDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "✨ Agregar topping extra",
            description = """
                    Añade un topping adicional a un jugo existente.
                    Si el jugo ya tiene toppings, el nuevo se agregará a la lista existente.
                    
                    El precio se actualizará automáticamente (+$1000 por topping).
                    """,
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Topping agregado exitosamente",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Topping agregado",
                                    value = "✨ Topping agregado: granola"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "🚫 Jugo no encontrado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Jugo no encontrado",
                                    value = "🚫 Jugo no encontrado con ID: 999"
                            )
                    )
            )
    })
    public @interface AgregarToppingDocumentation {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "🗑️ Eliminar jugo",
            description = """
                    Elimina un jugo del sistema. Esta operación es útil para:
                    - Cancelar pedidos que no se pueden completar
                    - Limpiar registros de prueba
                    - Gestionar errores en pedidos
                    
                    ⚠️ Esta acción no se puede deshacer.
                    """,
            tags = {"🍹 Gestión de Jugos"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Jugo eliminado exitosamente",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Eliminación exitosa",
                                    value = "🗑️ Jugo eliminado exitosamente"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "🚫 Jugo no encontrado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Jugo no encontrado",
                                    value = "🚫 Jugo no encontrado con ID: 999"
                            )
                    )
            )
    })
    public @interface EliminarJugoDocumentation {}
}