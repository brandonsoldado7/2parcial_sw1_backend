package com.segundoparcialsw1.diagramadorinteligente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.segundoparcialsw1.diagramadorinteligente")
@EnableJpaAuditing 
public class DiagramaUmlApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(DiagramaUmlApplication.class, args);
    }
}
