# 🧃 Máquina de Jugos con Arquitectura Hexagonal (Spring Boot + PostgreSQL)

Este proyecto es una simulación completa de una **máquina que hace jugos**, construida con **Spring Boot** usando el enfoque de **arquitectura hexagonal** (también conocida como Ports and Adapters). Todo el flujo, desde que un cliente hace un pedido por WhatsApp hasta que el jugo se guarda en la base de datos, está representado en código real, organizado y documentado.

---

## 🎯 Objetivo del Proyecto

- Entender la **arquitectura hexagonal** aplicándola en un proyecto divertido.
- Aprender cómo interactúan **entidades**, **servicios**, **casos de uso**, **repositorios**, **controladores** y **mappers**.
- Ver cómo se conecta un backend Java con una base de datos PostgreSQL.

---

## ⚙️ Tecnologías usadas

- Java 17
- Spring Boot 3.x
- PostgreSQL
- Spring Data JPA
- MapStruct
- Lombok
- Arquitectura Hexagonal

---

## 🧩 Analogía de Máquina de Jugos

| Agente                        | En la máquina de jugos               | Qué hace en el código                          |
|------------------------------|--------------------------------------|------------------------------------------------|
| Cliente                      | Persona que escribe por WhatsApp     | Lanza una petición HTTP POST con el pedido     |
| Controlador (`Controller`)   | Pantalla de pedidos                  | Recibe el pedido y lo manda al Use Case        |
| Use Case                     | Chef con recetas                     | Decide qué pasos ejecutar con ese pedido       |
| Servicio (`Service`)         | Licuadora, mezcladora, decoradora    | Ejecuta acciones concretas como mezclar, licuar, decorar |
| DTO                          | Hoja del pedido                      | Lo que el cliente pidió                        |
| Mapper                       | Traductor entre hoja y jugo real     | Convierte DTO ↔ Entidad                        |
| Entidad (`Entity`)           | Jugo preparado y servido             | Objeto final que se guarda                     |
| Repositorio (`Repository`)   | Nevera                               | Guarda los jugos listos (PostgreSQL)           |
| Puerto de entrada/salida     | Enchufes y cables de la máquina      | Conectan Use Cases con servicios o DB          |

---

## 🗂️ Estructura del Proyecto

```
com.jugoshexagonales
├── app
│   ├── controller
│   └── dto
├── domain
│   ├── entity
│   ├── ports
│   ├── service
│   └── usecase
├── infrastructure
│   ├── config
│   ├── mapper
│   └── repository
└── JugosApplication.java
```

---

## 🔁 Flujo de ejecución

1. El cliente hace un **pedido HTTP POST** al endpoint `/api/pedidos`.
2. El `PedidoController` recibe el DTO `PedidoRequest` y lo pasa al caso de uso `RealizarPedidoUseCase`.
3. El caso de uso llama al `JugoService` para preparar el jugo:
   - `mezclarIngredientes()`
   - `licuar()`
   - `endulzar()`
   - `decorar()`
4. Se crea una instancia de `JugoEntity`.
5. El mapper transforma el DTO a entidad y viceversa.
6. El repositorio guarda el jugo en la base de datos PostgreSQL.

---

## 📚 Endpoints disponibles (CRUD de jugos)

| Método | Ruta               | Acción                        |
|--------|--------------------|-------------------------------|
| POST   | `/api/pedidos`     | Crear un nuevo jugo           |
| GET    | `/api/pedidos`     | Listar todos los jugos        |
| GET    | `/api/pedidos/{id}`| Obtener un jugo por ID        |
| PUT    | `/api/pedidos/{id}`| Editar jugo existente         |
| DELETE | `/api/pedidos/{id}`| Eliminar un jugo              |

---

## 🛠️ Configuración de la base de datos (PostgreSQL)

Archivo: `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nevera_jugos
    username: postgres
    password: tu_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

---

## 🧪 Pruebas con Postman

Puedes usar este JSON como cuerpo para probar el endpoint POST:

```json
{
  "sabor": "fresa",
  "endulzante": "miel",
  "vaso": "grande"
}
```

---

## ✅ Por implementar

| Tarea                                      | Estado    |
|-------------------------------------------|-----------|
| Agregar validaciones a los pedidos        | ✅ Listo  |
| Implementar casos de uso como servicios   | ✅ Listo  |
| Agregar mapeo DTO ↔ entidad               | ✅ Listo  |
| Integrar PostgreSQL                       | ✅ Listo  |
| CRUD completo para jugos                  | ✅ Listo  |
| Documentar el proyecto en README          | ✅ Listo  |
| Incluir pruebas unitarias (JUnit)         | 🔲 Pendiente |
| Dockerizar la app + PostgreSQL            | 🔲 Pendiente |
| Swagger para documentación de la API      | 🔲 Pendiente |

---

## 📖 Recomendado para el niño curioso

> Imagínate que tú eres un chef y alguien te dice por WhatsApp: “¡Hazme un jugo de mango con azúcar y vaso mediano!”… Entonces tú sigues unos pasos:
> 
> 1. Tomas los ingredientes (como el DTO 📄)
> 2. Usas la licuadora (el servicio 🔄)
> 3. Lo viertes en un vaso (la entidad 🍹)
> 4. Lo guardas en la nevera (repositorio 📦)
> 
> ¡Y todo esto es lo que hace esta aplicación!

---

¿Tienes ideas para nuevos sabores, pasos como refrigerar o agregar hielo? Puedes extender este proyecto como quieras. ¡Disfruta desarrollando jugos! 🧃💻
