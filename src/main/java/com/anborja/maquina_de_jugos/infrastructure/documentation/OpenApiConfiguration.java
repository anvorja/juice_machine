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
                                ## API REST para gestión de una máquina de jugos
                                
                                Esta API permite:
                                - 🥤 Crear pedidos de jugos personalizados
                                - 📋 Gestionar el estado de los pedidos
                                - 📊 Generar reportes de ventas
                                - 🔍 Filtrar por sabor, cliente, estado y tamaño
                                - ✨ Agregar toppings adicionales
                                
                                ### Estados de los jugos:
                                - **PREPARANDO**: El jugo está siendo elaborado
                                - **LISTO**: El jugo está terminado y listo para entregar
                                - **ENTREGADO**: El jugo fue entregado al cliente
                                
                                ### Sabores disponibles:
                                mango, fresa, naranja, piña, manzana, pera, maracuyá, 
                                guayaba, lulo, mora, banano, papaya
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
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.maquinajugos.com")
                                .description("Servidor de producción")))
                .tags(List.of(
                        new Tag()
                                .name("🍹 Gestión de Jugos")
                                .description("Operaciones CRUD para pedidos de jugos"),
                        new Tag()
                                .name("📊 Reportes")
                                .description("Generación de reportes de ventas y estadísticas"),
                        new Tag()
                                .name("🔍 Búsquedas")
                                .description("Filtrado y búsqueda de jugos por diferentes criterios")
                ));
    }
}