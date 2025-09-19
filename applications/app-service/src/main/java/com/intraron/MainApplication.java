package com.intraron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        LOGGER.info("El microservicio de solicitudes ha iniciado correctamente.");
    }

    public void onApplicationShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("El microservicio de solicitudes se está deteniendo.");
        }));
    }
}
