package com.licencias.licencias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LicenciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenciasApplication.class, args);
    }
}
