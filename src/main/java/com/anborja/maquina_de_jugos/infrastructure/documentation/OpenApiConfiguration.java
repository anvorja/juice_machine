package com.anborja.maquina_de_jugos.infrastructure.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🍹 Máquina de Jugos API")
                        .description("""
                                ## API REST para gestión de una máquina de jugos naturales
                                
                                Esta API permite gestionar completamente el ciclo de vida de los pedidos de jugos:
                                
                                ### 🚀 Funcionalidades principales:
                                - 🥤 **Crear pedidos personalizados** con sabores, tamaños y toppings
                                - 📋 **Gestionar estados** (PREPARANDO → LISTO → ENTREGADO)
                                - 🔍 **Búsquedas avanzadas** por sabor, cliente, estado y tamaño
                                - 📊 **Reportes de ventas** con estadísticas detalladas
                                - ✨ **Modificaciones dinámicas** (agregar toppings, cambiar estados)
                                
                                ### 📈 Estados del flujo de trabajo:
                                - **PREPARANDO**: El jugo está siendo elaborado
                                - **LISTO**: El jugo está terminado y listo para entregar
                                - **ENTREGADO**: El jugo fue entregado al cliente
                                
                                ### 🥭 Sabores disponibles:
                                `mango` • `fresa` • `naranja` • `piña` • `manzana` • `pera` • `maracuyá` • `guayaba` • `lulo` • `mora` • `banano` • `papaya`
                                
                                ### 🥤 Tamaños de vaso:
                                - **Pequeño**: $3,000 (300ml)
                                - **Mediano**: $4,000 (500ml)
                                - **Grande**: $5,000 (700ml)
                                
                                *Los toppings agregan $1,000 adicionales al precio base*
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Máquina de Jugos")
                                .email("contacto@maquinajugos.com")
                                .url("https://github.com/tu-usuario/maquina-jugos"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("🛠️ Servidor de desarrollo"),
                        new Server()
                                .url("https://api.maquinajugos.com")
                                .description("🌐 Servidor de producción")))
                .tags(List.of(
                        new Tag()
                                .name("🍹 Gestión de Jugos")
                                .description("Operaciones CRUD principales para pedidos de jugos"),
                        new Tag()
                                .name("🔍 Búsquedas")
                                .description("Filtrado y búsqueda de jugos por diferentes criterios"),
                        new Tag()
                                .name("📊 Reportes")
                                .description("Generación de reportes de ventas y estadísticas")
                ));
    }
}