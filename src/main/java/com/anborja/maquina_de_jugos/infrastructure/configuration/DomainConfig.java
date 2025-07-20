package com.anborja.maquina_de_jugos.infrastructure.configuration;

import com.anborja.maquina_de_jugos.domain.port.inbound.JugoServicePort;
import com.anborja.maquina_de_jugos.domain.service.JugoPreparationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {
    @Bean
    public JugoServicePort jugoServicePort() {
        // “Hola Spring, cuando alguien pida el servicio de jugos JugoServicePort, dale
        // este cocinero llamado JugoPreparationService quien sabe hacerlo”.
        return new JugoPreparationService();
    }
}
