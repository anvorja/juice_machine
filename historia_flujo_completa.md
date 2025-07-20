# 🧃 La Historia Completa de la Máquina de Jugos
## Arquitectura Hexagonal Explicada como una Aventura

---

## 🎬 **PERSONAJES DE NUESTRA HISTORIA**

| 👤 Personaje | 🏭 En la Máquina | 💻 En el Código | 🎯 Su Trabajo |
|---------------|-------------------|------------------|----------------|
| **Cliente** | Persona con sed | Usuario con Postman/App | Hace pedidos de jugos |
| **Recepcionista** | Pantalla de pedidos | `JugoController` | Recibe y valida pedidos |
| **Chef Principal** | Supervisor de cocina | `CrearJugoUseCase` | Decide qué hacer con cada pedido |
| **Especialista en Frutas** | Licuadora inteligente | `JugoPreparationService` | Sabe cómo preparar cada jugo |
| **Bibliotecario** | Nevera organizadora | `JugoRepositoryAdapter` | Guarda y busca jugos |
| **Traductor** | Convertidor de pedidos | `JugoMapperFactory` | Traduce entre formatos |
| **Gerente de Errores** | Solucionador de problemas | `GlobalExceptionHandler` | Maneja cuando algo sale mal |
| **Detective** | Investigador de pedidos | `BuscarJugoPorIdUseCase` | Encuentra jugos específicos |
| **Analista** | Experto en reportes | `GenerarReporteVentasUseCase` | Crea estadísticas de ventas |

---

## 🥭 **SABORES DISPONIBLES EN NUESTRA MÁQUINA**

```java
mango • fresa • naranja • piña • manzana • pera • maracuyá • guayaba • lulo • mora • banano • papaya
```

---

## 💰 **SISTEMA DE PRECIOS AUTOMÁTICO**

| 🥤 Tamaño | 💵 Precio Base |
|-----------|----------------|
| Pequeño (300ml) | $3,000 |
| Mediano (500ml) | $4,000 |
| Grande (700ml) | $5,000 |

**✨ Toppings:** +$1,000 cada uno (granola, chía, coco, etc.)

---

## 📱 **HISTORIA 1: "El Pedido de María por WhatsApp"**

### 🎭 **Escena 1: El Cliente Hace el Pedido**

```
📱 MARÍA: "Hola! Quiero un jugo de mango grande con azúcar, hielo y granola para María"
```

**🔍 Lo que pasa en el código:**
```json
POST http://localhost:8080/api/jugos
{
    "sabor": "mango",
    "conAzucar": true,
    "conHielo": true,
    "topping": "granola",
    "tamanoVaso": "grande",
    "clienteNombre": "María"
}
```

---

### 🎭 **Escena 2: La Recepcionista Recibe el Pedido**

```
👩‍💼 RECEPCIONISTA: "¡Nuevo pedido! Veamos... María quiere mango grande con granola. 
                     Voy a verificar que todo esté bien escrito..."
```

**🔍 Flujo del código:**
```java
// 📁 JugoController.java
@PostMapping
public ResponseEntity<JugoResponseDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
    System.out.println("📱 NUEVO PEDIDO POR WHATSAPP: " + pedidoDTO.getClienteNombre() + 
                      " quiere un jugo de " + pedidoDTO.getSabor());
    
    // ✅ 1. Validar que el pedido esté completo
    // ✅ 2. Pasar al Chef Principal
    JugoEntity jugo = crearJugoUseCase.ejecutar(pedidoDTO);
    
    // ✅ 3. Traducir respuesta para el cliente
    JugoResponseDTO response = mapper.toResponseDTO(jugo);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

**🎯 Comunicación:**
- **Recibe de:** Cliente (JSON con `@Valid` para validaciones)
- **Valida:** Que tenga sabor, tamaño y nombre del cliente (usando Bean Validation)
- **Envía a:** Chef Principal (`CrearJugoUseCase`)

---

### 🎭 **Escena 3: El Chef Principal Organiza la Preparación**

```
👨‍🍳 CHEF: "¡Perfecto! Nuevo pedido de María. Vamos a preparar esto paso a paso:
           1. Primero llamo al especialista en frutas para que prepare el jugo
           2. Luego le digo al bibliotecario que lo guarde en la nevera
           3. Finalmente envío un WhatsApp a María"
```

**🔍 Flujo del código:**
```java
// 📁 CrearJugoUseCase.java
@Service
@RequiredArgsConstructor
public class CrearJugoUseCase {

    private final JugoServicePort jugoService;
    private final JugoRepositoryPort repository;

    public JugoEntity ejecutar(PedidoDTO pedido) {
        System.out.println("🎯 INICIANDO PEDIDO PARA: " + pedido.getClienteNombre());

        // 🍹 PASO 1: Llamar al especialista en frutas
        JugoEntity jugo = jugoService.prepararJugo(
                pedido.getSabor(),
                pedido.isConAzucar(),
                pedido.isConHielo(),
                pedido.getTopping(),
                pedido.getTamanoVaso(),
                pedido.getClienteNombre()
        );

        // 💾 PASO 2: Pedirle al bibliotecario que lo guarde
        JugoEntity jugoGuardado = repository.guardar(jugo);

        // 📱 PASO 3: Enviar WhatsApp de confirmación
        String mensaje = jugoService.simularMensajeWhatsApp(
                pedido.getClienteNombre(), 
                "LISTO"
        );
        System.out.println("📱 " + mensaje);

        return jugoGuardado;
    }
}
```

**🎯 Comunicación:**
- **Recibe de:** Recepcionista (`JugoController`)
- **Coordina con:** Especialista en Frutas (`JugoPreparationService`) + Bibliotecario (`JugoRepositoryAdapter`)
- **Devuelve a:** Recepcionista (jugo preparado)

---

### 🎭 **Escena 4: El Especialista en Frutas Trabaja su Magia**

```
🥭 ESPECIALISTA: "¡Excelente! Un mango grande con azúcar, hielo y granola para María.
                 Vamos a seguir la receta paso a paso..."
```

**🔍 Flujo del código:**
```java
// 📁 JugoPreparationService.java
public class JugoPreparationService implements JugoServicePort {

    private final List<String> saboresDisponibles = Arrays.asList(
            "mango", "fresa", "naranja", "piña", "manzana", "pera", "maracuyá",
            "guayaba", "lulo", "mora", "banano", "papaya"
    );

    @Override
    public JugoEntity prepararJugo(String sabor, boolean conAzucar, boolean conHielo, 
                                  String topping, String tamanoVaso, String clienteNombre) {
        
        System.out.println("🍹 === INICIANDO PREPARACIÓN DE JUGO ===");
        
        // ✅ Verificar que tengamos mango disponible
        if (!saboresDisponibles.contains(sabor.toLowerCase())) {
            throw new SaborNoDisponibleException(sabor);
        }
        
        // 🥭 PASO 1: Mezclar ingredientes
        mezclarIngredientes(sabor);      // "🥭 Mezclando mango fresco..."
        
        // 🌀 PASO 2: Licuar
        licuar();                        // "🌀 Licuando a máxima velocidad..."
        
        // 🍯 PASO 3: Endulzar (solo si pidió azúcar)
        if (conAzucar) {
            endulzar();                  // "🍯 Agregando azúcar al gusto..."
        }
        
        // 🧊 PASO 4: Agregar hielo (solo si pidió hielo)
        if (conHielo) {
            agregarHielo();              // "🧊 Añadiendo cubitos de hielo..."
        }
        
        // 🥤 PASO 5: Servir en vaso grande
        servirEnVaso(tamanoVaso);        // "🥤 Sirviendo en vaso grande..."
        
        // ✨ PASO 6: Decorar con granola
        if (topping != null && !topping.isEmpty()) {
            decorarConTopping(topping);  // "✨ Decorando con granola..."
        }
        
        System.out.println("✅ ¡Jugo de " + sabor + " listo para " + clienteNombre + "!");
        
        // 🎯 Crear el jugo físico con precio automático
        return new JugoEntity(sabor, conAzucar, conHielo, topping, tamanoVaso, clienteNombre);
    }
    
    // Métodos que simulan cada paso de la preparación
    private void simularTiempo(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

**💰 Cálculo automático de precio en JugoEntity:**
```java
private Double calcularPrecio() {
    double precioBase = switch (tamanoVaso != null ? tamanoVaso.toLowerCase() : "mediano") {
        case "pequeño" -> 3000.0;
        case "grande" -> 5000.0;
        default -> 4000.0; // mediano
    };

    if (topping != null && !topping.isEmpty()) {
        precioBase += 1000.0;
    }

    return precioBase;
}
```

**🎯 Comunicación:**
- **Recibe de:** Chef Principal (`CrearJugoUseCase`)
- **Valida:** Que el sabor esté disponible
- **Ejecuta:** Secuencia de preparación física con simulación de tiempo
- **Calcula:** Precio automáticamente según tamaño y toppings
- **Devuelve a:** Chef Principal (jugo preparado)

---

### 🎭 **Escena 5: El Bibliotecario Guarda el Jugo en la Nevera**

```
📚 BIBLIOTECARIO: "¡Perfecto! Voy a guardar este delicioso jugo de mango en la nevera.
                  Le pondré una etiqueta con el ID #1 para encontrarlo fácilmente."
```

**🔍 Flujo del código:**
```java
// 📁 JugoRepositoryAdapter.java
@Component
@RequiredArgsConstructor
public class JugoRepositoryAdapter implements JugoRepositoryPort {

    private final JpaJugoRepository jpaRepository;

    @Override
    public JugoEntity guardar(JugoEntity jugo) {
        System.out.println("💾 Guardando jugo en la nevera (base de datos)...");
        return jpaRepository.save(jugo);
    }
}
```

**🗃️ La nevera física (PostgreSQL):**
```java
// 📁 JpaJugoRepository.java
@Repository
public interface JpaJugoRepository extends JpaRepository<JugoEntity, Long> {
    List<JugoEntity> findBySaborContainingIgnoreCase(String sabor);
    List<JugoEntity> findByClienteNombreContainingIgnoreCase(String clienteNombre);
    List<JugoEntity> findByEstado(String estado);
    
    @Query("SELECT j FROM JugoEntity j WHERE j.tamanoVaso = :tamano")
    List<JugoEntity> findByTamanoVaso(@Param("tamano") String tamano);
}
```

**🎯 Comunicación:**
- **Recibe de:** Chef Principal (`CrearJugoUseCase`)
- **Guarda en:** Nevera física (`PostgreSQL`)
- **Asigna:** ID único automáticamente
- **Devuelve a:** Chef Principal (jugo con ID)

---

### 🎭 **Escena 6: El Traductor Prepara la Respuesta**

```
🌐 TRADUCTOR: "Ahora voy a convertir toda la información del jugo en un formato 
              que el cliente pueda entender fácilmente por WhatsApp."
```

**🔍 Flujo del código:**
```java
// 📁 JugoMapperFactory.java
@Component
public class JugoMapperFactory {

    public JugoResponseDTO toResponseDTO(JugoEntity entity) {
        return new JugoResponseDTO(
                entity.getId(),          // ID: 1
                entity.getSabor(),       // "mango"
                entity.isConAzucar(),    // true
                entity.isConHielo(),     // true
                entity.getTopping(),     // "granola"
                entity.getTamanoVaso(),  // "grande"
                entity.getEstado(),      // "PREPARANDO"
                entity.getFechaPedido(), // "2025-07-20T19:30:00"
                entity.getPrecio(),      // 6000.0
                entity.getClienteNombre() // "María"
        );
    }
}
```

**🎯 Comunicación:**
- **Recibe de:** Recepcionista (`JugoController`)
- **Convierte:** JugoEntity → JugoResponseDTO
- **Devuelve a:** Recepcionista (formato para cliente)

---

### 🎭 **Escena 7: Respuesta al Cliente**

```
📱 RESPUESTA A MARÍA:
{
  "id": 1,
  "sabor": "mango",
  "conAzucar": true,
  "conHielo": true,
  "topping": "granola",
  "tamanoVaso": "grande",
  "estado": "PREPARANDO",
  "fechaPedido": "2025-07-20T19:30:00",
  "precio": 6000.0,
  "clienteNombre": "María"
}

📱 NOTIFICACIÓN WHATSAPP: "¡María, tu jugo está listo! 🎉 Puedes venir a recogerlo"
```

---

## 🔄 **HISTORIA 2: "El Chef Actualiza el Estado del Jugo"**

### 📱 **El chef marca el jugo como LISTO:**

```
PATCH http://localhost:8080/api/jugos/1/estado?estado=LISTO
```

### 🎭 **Flujo de actualización:**

```java
// 📁 ActualizarJugoUseCase.java
@Service
@RequiredArgsConstructor
public class ActualizarJugoUseCase {

    private final JugoRepositoryPort repository;
    private final JugoServicePort jugoService;

    public void cambiarEstado(Long id, String nuevoEstado) {
        System.out.println("🔄 Cambiando estado del jugo " + id + " a: " + nuevoEstado);
        repository.actualizarEstado(id, nuevoEstado);

        // Simular notificación
        String mensaje = jugoService.simularMensajeWhatsApp("Cliente", nuevoEstado);
        System.out.println("📱 " + mensaje);
    }
}
```

**🎯 Estados del flujo:**
- **PREPARANDO** → El jugo está siendo elaborado
- **LISTO** → El jugo está terminado y listo para entregar
- **ENTREGADO** → El jugo fue entregado al cliente

---

## 🔍 **HISTORIA 3: "Juan Busca Su Jugo por ID"**

### 📱 **Juan pregunta:** "¿Cómo está mi pedido #1?"

```
GET http://localhost:8080/api/jugos/1
```

### 🎭 **Flujo de búsqueda:**

```java
// 📁 BuscarJugoPorIdUseCase.java
@Service
@RequiredArgsConstructor
public class BuscarJugoPorIdUseCase {

    private final JugoRepositoryPort jugoRepositoryPort;

    public JugoEntity ejecutar(Long id){
        System.out.println("🔍 Buscando jugo con ID: " + id);
        return jugoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new JugoNotFoundException(id));
    }
}
```

**🎭 Flujo completo:**
```
👩‍💼 RECEPCIONISTA: "Juan quiere saber sobre el jugo #1"
         ↓
🔍 DETECTIVE (BuscarJugoPorIdUseCase): "Voy a buscarlo en la nevera"
         ↓
📚 BIBLIOTECARIO: "Aquí está el jugo #1 de María"
         ↓
🌐 TRADUCTOR: "Lo convierto a formato legible"
         ↓
📱 JUAN RECIBE: Información completa del jugo #1
```

---

## 📊 **HISTORIA 4: "El Gerente Pide un Reporte de Ventas"**

### 📱 **Gerente pregunta:** "¿Cómo van las ventas de hoy?"

```
GET http://localhost:8080/api/jugos/reporte
```

### 🎭 **Flujo del reporte:**

```java
// 📁 GenerarReporteVentasUseCase.java
@Service
@RequiredArgsConstructor
public class GenerarReporteVentasUseCase {

    public record ReporteVentas(
            LocalDate fecha,
            int jugosPreparando,
            int jugosListos,
            int jugosEntregados,
            Map<String, Long> saboresMasVendidos,
            Map<String, Long> tamanosMasVendidos,
            double ingresosTotales
    ) {}

    public ReporteVentas ejecutar() {
        System.out.println("📊 === GENERANDO REPORTE DE VENTAS ===");

        // Usar métodos existentes del repository
        List<JugoEntity> preparando = repository.buscarPorEstado("PREPARANDO");
        List<JugoEntity> listos = repository.buscarPorEstado("LISTO");
        List<JugoEntity> entregados = repository.buscarPorEstado("ENTREGADO");

        // Estadísticas de sabores más vendidos
        Map<String, Long> saboresMasVendidos = entregados.stream()
                .collect(Collectors.groupingBy(JugoEntity::getSabor, Collectors.counting()));

        // Estadísticas de tamaños más vendidos
        Map<String, Long> tamanosMasVendidos = entregados.stream()
                .collect(Collectors.groupingBy(JugoEntity::getTamanoVaso, Collectors.counting()));

        // Ingresos totales
        double ingresosTotales = entregados.stream()
                .mapToDouble(JugoEntity::getPrecio)
                .sum();

        return new ReporteVentas(/*...*/);
    }
}
```

**📱 GERENTE RECIBE:**
```json
{
  "fecha": "2025-07-20",
  "jugosPreparando": 1,
  "jugosListos": 2,
  "jugosEntregados": 5,
  "saboresMasVendidos": {"mango": 3, "fresa": 2},
  "tamanosMasVendidos": {"grande": 6, "mediano": 4},
  "ingresosTotales": 24000.0
}
```

---

## 🛡️ **HISTORIA 5: "Cuando Algo Sale Mal"**

### 😱 **Cliente pide sabor inexistente:**

```json
POST http://localhost:8080/api/jugos
{
    "sabor": "dragonfruit",  // ← ¡No tenemos!
    "tamanoVaso": "grande",
    "clienteNombre": "Pedro"
}
```

### 🎭 **Flujo de manejo de errores:**

```java
// 📁 GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaborNoDisponibleException.class)
    public ResponseEntity<String> handleSaborNoDisponible(SaborNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("🍹 " + ex.getMessage());
    }

    @ExceptionHandler(JugoNotFoundException.class)
    public ResponseEntity<String> handleJugoNotFound(JugoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("🚫 " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage()
                : "Error de validación en los datos enviados";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("📝 Error de validación: " + errorMessage);
    }
}
```

**🎭 Flujo de error:**
```
👩‍💼 RECEPCIONISTA: "Pedro quiere dragonfruit"
         ↓
👨‍🍳 CHEF PRINCIPAL: "Le digo al especialista"
         ↓
🥭 ESPECIALISTA: "¡ERROR! No tengo dragonfruit"
         ↓ (lanza SaborNoDisponibleException)
🚨 GERENTE DE ERRORES (GlobalExceptionHandler): "Intercepto el error"
         ↓
📱 PEDRO RECIBE: "🍹 El sabor 'dragonfruit' no está disponible en este momento"
```

---

## 🔄 **FLUJO COMPLETO EN DIAGRAMA**

```
📱 CLIENTE (PedidoDTO)
    ↓ (JSON con pedido)
📥 PUERTO DE ENTRADA (REST API)
    ↓
👩‍💼 RECEPCIONISTA (JugoController)
    ↓ (PedidoDTO validado)
👨‍🍳 CHEF PRINCIPAL (CrearJugoUseCase)
    ↓ (instrucciones de preparación)
🥭 ESPECIALISTA EN FRUTAS (JugoPreparationService)
    ↓ (jugo preparado)
👨‍🍳 CHEF PRINCIPAL
    ↓ (jugo para guardar)
📚 BIBLIOTECARIO (JugoRepositoryAdapter)
    ↓ (petición de guardado)
🗃️ NEVERA FÍSICA (PostgreSQL - JpaJugoRepository)
    ↓ (jugo guardado con ID)
📚 BIBLIOTECARIO
    ↓ (jugo guardado)
👨‍🍳 CHEF PRINCIPAL
    ↓ (jugo final)
👩‍💼 RECEPCIONISTA
    ↓ (conversión a respuesta)
🌐 TRADUCTOR (JugoMapperFactory)
    ↓ (JugoResponseDTO)
👩‍💼 RECEPCIONISTA
    ↓ (JSON de respuesta)
📤 PUERTO DE SALIDA (REST API)
    ↓
📱 CLIENTE (respuesta + WhatsApp simulado)
```

---

## 🏗️ **ARQUITECTURA HEXAGONAL: Los Hexágonos Explicados**

### 🎯 **HEXÁGONO INTERIOR (Domain):**
```
┌─────────────────────────────────┐
│        🧠 CENTRO CEREBRAL       │
│                                 │
│  JugoEntity (el jugo real)      │
│  JugoServicePort (contrato)     │
│  JugoRepositoryPort (contrato)  │
│                                 │
│  ✅ NO conoce la tecnología     │
│  ✅ Solo reglas de negocio      │
│  ✅ Cálculo de precios          │
│  ✅ Estados del jugo            │
└─────────────────────────────────┘
```

### 📋 **HEXÁGONO MEDIO (Application):**
```
┌─────────────────────────────────┐
│      👨‍🍳 CENTRO DE COMANDO      │
│                                 │
│  CrearJugoUseCase               │
│  BuscarJugoPorIdUseCase         │
│  ActualizarJugoUseCase          │
│  GenerarReporteVentasUseCase    │
│  PedidoDTO / JugoResponseDTO    │
│  GlobalExceptionHandler         │
│                                 │
│  ✅ Orquesta las operaciones    │
│  ✅ Define casos de uso         │
│  ✅ Maneja errores              │
└─────────────────────────────────┘
```

### 🔌 **HEXÁGONO EXTERIOR (Infrastructure):**
```
┌─────────────────────────────────┐
│     🏭 CONEXIONES EXTERNAS      │
│                                 │
│  JugoController (REST API)      │
│  JugoPreparationService         │
│  JugoRepositoryAdapter          │
│  JpaJugoRepository              │
│  JugoMapperFactory              │
│  DomainConfig                   │
│                                 │
│  ✅ Se conecta con el mundo     │
│  ✅ Implementa los contratos    │
│  ✅ Tecnologías específicas     │
└─────────────────────────────────┘
```

---

## 🎓 **¿POR QUÉ ES GENIAL ESTA ARQUITECTURA?**

### ✅ **Para el niño:**
1. **🧩 Cada pieza tiene un trabajo específico** - como en un rompecabezas
2. **🔄 Fácil de cambiar** - si queremos cambiar WhatsApp por Telegram, solo cambiamos el controller
3. **🧪 Fácil de probar** - podemos probar cada pieza por separado
4. **📚 Fácil de entender** - cada archivo tiene un propósito claro

### ✅ **Para el desarrollador:**
1. **🎯 Separación de responsabilidades** - cada capa hace lo suyo
2. **🔄 Inversión de dependencias** - el centro no depende de los bordes
3. **🧪 Testeable** - puedes probar lógica sin base de datos
4. **🔧 Mantenible** - cambios en una capa no afectan otras
5. **📦 Modular** - cada componente es independiente

---

## 🎮 **¡ENDPOINTS DISPONIBLES PARA PROBAR!**

### 🧪 **Gestión de Jugos:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/jugos` | Crear nuevo jugo |
| GET | `/api/jugos` | Listar todos los jugos |
| GET | `/api/jugos/{id}` | Buscar jugo por ID |
| DELETE | `/api/jugos/{id}` | Eliminar jugo |

### 🔍 **Búsquedas Especializadas:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/jugos/sabor/{sabor}` | Buscar por sabor |
| GET | `/api/jugos/cliente/{nombre}` | Buscar por cliente |
| GET | `/api/jugos/estado/{estado}` | Buscar por estado |
| GET | `/api/jugos/tamano/{tamano}` | Buscar por tamaño |

### 🔄 **Actualizaciones:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| PATCH | `/api/jugos/{id}/estado` | Cambiar estado |
| PATCH | `/api/jugos/{id}/topping` | Agregar topping |

### 📊 **Reportes:**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/jugos/reporte` | Reporte de ventas |

---

## 🧪 **SECUENCIA DE PRUEBAS PARA ENTENDER EL FLUJO:**

### 1. **Crear un jugo y ver todo el proceso:**
```bash
POST http://localhost:8080/api/jugos
Content-Type: application/json

{
    "sabor": "mango",
    "conAzucar": true,
    "conHielo": true,
    "topping": "granola",
    "tamanoVaso": "grande",
    "clienteNombre": "María García"
}
```

### 2. **Buscar el jugo que creaste:**
```bash
GET http://localhost:8080/api/jugos/1
```

### 3. **Cambiar su estado:**
```bash
PATCH http://localhost:8080/api/jugos/1/estado?estado=LISTO
```

### 4. **Agregar un topping extra:**
```bash
PATCH http://localhost:8080/api/jugos/1/topping?topping=chía
```

### 5. **Ver el reporte:**
```bash
GET http://localhost:8080/api/jugos/reporte
```

### 6. **Buscar jugos por sabor:**
```bash
GET http://localhost:8080/api/jugos/sabor/mango
```

### 7. **Buscar jugos por cliente:**
```bash
GET http://localhost:8080/api/jugos/cliente/María
```

### 8. **Probar un error:**
```bash
POST http://localhost:8080/api/jugos
Content-Type: application/json

{
    "sabor": "kiwi",  // ← ¡Sabor no disponible!
    "tamanoVaso": "grande",
    "clienteNombre": "Pedro"
}
```

### 🎯 **¡En cada paso verás los mensajes en consola mostrando el flujo completo!**

---

## 📋 **CONFIGURACIÓN DE LA BASE DE DATOS**

### 🗃️ **PostgreSQL Configuration (application.yml):**
```yaml
spring:
  application:
    name: maquina-jugos

  datasource:
    url: jdbc:postgresql://localhost:5432/nevera_jugos
    username: postgres
    password: superapostgres
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

### 🗄️ **Estructura de la tabla `jugos`:**
```sql
CREATE TABLE jugos (
    id BIGSERIAL PRIMARY KEY,
    sabor VARCHAR(50) NOT NULL,
    con_azucar BOOLEAN,
    con_hielo BOOLEAN,
    topping VARCHAR(100),
    tamano_vaso VARCHAR(20),
    estado VARCHAR(20),
    fecha_pedido TIMESTAMP,
    precio DECIMAL(10,2),
    cliente_nombre VARCHAR(100)
);
```

---

## 📚 **VALIDACIONES IMPLEMENTADAS**

### ✅ **Bean Validation en PedidoDTO:**
```java
public class PedidoDTO {

    @NotBlank(message = "El sabor es obligatorio")
    private String sabor;

    private boolean conAzucar;
    private boolean conHielo;
    private String topping;

    @NotBlank(message = "El tamaño del vaso es obligatorio")
    private String tamanoVaso; // pequeño, mediano, grande

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;
}
```

### 🛡️ **Manejo de Excepciones:**
- **SaborNoDisponibleException**: Cuando se pide un sabor que no existe
- **JugoNotFoundException**: Cuando se busca un jugo que no existe
- **MethodArgumentNotValidException**: Cuando fallan las validaciones Bean Validation
- **Exception**: Manejo genérico para errores inesperados

---

## 🎨 **DOCUMENTACIÓN SWAGGER INCLUIDA**

El proyecto incluye documentación completa con Swagger/OpenAPI:

### 📖 **Acceso a la documentación:**
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

### 🏷️ **Tags organizados:**
- 🍹 **Gestión de Jugos**: Operaciones CRUD principales
- 🔍 **Búsquedas**: Filtrado por diferentes criterios
- 📊 **Reportes**: Generación de estadísticas

---

## 🔧 **TECNOLOGÍAS Y DEPENDENCIAS UTILIZADAS**

### 📦 **Principales (pom.xml):**
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.38</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### ☕ **Versiones:**
- **Java**: 21
- **Spring Boot**: 3.5.3
- **PostgreSQL**: Driver más reciente
- **Lombok**: 1.18.38

---

## 🎭 **MENSAJES DE CONSOLA PARA SEGUIR EL FLUJO**

### 📱 **Al crear un jugo verás:**
```
📱 NUEVO PEDIDO POR WHATSAPP: María quiere un jugo de mango
🎯 INICIANDO PEDIDO PARA: María
🍹 === INICIANDO PREPARACIÓN DE JUGO ===
🥭 Mezclando mango fresco...
🌀 Licuando a máxima velocidad...
🍯 Agregando azúcar al gusto...
🧊 Añadiendo cubitos de hielo...
🥤 Sirviendo en vaso grande...
✨ Decorando con granola...
✅ ¡Jugo de mango listo para María!
💾 Guardando jugo en la nevera (base de datos)...
📱 ¡María, tu jugo está listo! 🎉 Puedes venir a recogerlo
```

### 🔍 **Al buscar un jugo:**
```
🔍 Buscando jugo con ID: 1
```

### 🔄 **Al cambiar estado:**
```
🔄 Cambiando estado del jugo 1 a: LISTO
📱 Hola Cliente! Tu jugo está siendo preparado 🍹
```

### 📊 **Al generar reporte:**
```
📊 === GENERANDO REPORTE DE VENTAS ===
📈 Reporte generado - Total entregados: 5, Ingresos: $24000
```

---

## 🚀 **EXTENSIONES FUTURAS POSIBLES**

### 🔮 **Ideas para expandir el proyecto:**

1. **🔐 Autenticación y Autorización**
   - Spring Security para proteger endpoints
   - Roles: Cliente, Empleado, Gerente

2. **📱 Integración Real con WhatsApp**
   - API de WhatsApp Business
   - Notificaciones automáticas reales

3. **💳 Sistema de Pagos**
   - Integración con pasarelas de pago
   - Gestión de facturas

4. **📊 Dashboard Web**
   - Frontend con React/Angular
   - Gráficos en tiempo real

5. **🔄 Gestión de Inventario**
   - Control de frutas disponibles
   - Alertas de stock bajo

6. **🧪 Testing Completo**
   - Pruebas unitarias con JUnit 5
   - Pruebas de integración
   - TestContainers para PostgreSQL

7. **🐳 Containerización**
   - Docker Compose
   - Deployment con Kubernetes

8. **📈 Métricas y Monitoring**
   - Spring Boot Actuator
   - Prometheus + Grafana

---

## 🎉 **CONCLUSIÓN**

¡Felicitaciones! Ahora entiendes completamente cómo funciona una máquina de jugos construida con arquitectura hexagonal. Este proyecto demuestra:

### 🎯 **Conceptos Clave Aplicados:**
- **Arquitectura Hexagonal (Ports & Adapters)**
- **Inversión de Dependencias**
- **Separación de Responsabilidades**
- **Clean Architecture**
- **Domain-Driven Design (DDD)**

### 🧩 **Patrones Implementados:**
- **Repository Pattern**: Para acceso a datos
- **Use Case Pattern**: Para lógica de negocio
- **Adapter Pattern**: Para conectar con tecnologías externas
- **Factory Pattern**: Para mapeo de objetos
- **Exception Handling**: Para manejo centralizado de errores

### 🏆 **Beneficios Logrados:**
- **Código mantenible y legible**
- **Fácil testing y depuración**
- **Flexibilidad para cambios futuros**
- **Separación clara de responsabilidades**
- **Documentación completa y clara**

**¡Es como una orquesta donde cada músico (clase) toca su parte perfectamente para crear una hermosa sinfonía de jugos!** 🎵🧃✨

### 📚 **Para aprender más:**
- Arquitectura Hexagonal de Alistair Cockburn
- Clean Architecture de Robert C. Martin
- Domain-Driven Design de Eric Evans
- Patterns of Enterprise Application Architecture de Martin Fowler