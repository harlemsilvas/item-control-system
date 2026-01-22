package br.com.harlemsilvas.itemcontrol.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Aplicação principal da API REST do Item Control System.
 * 
 * @author Harlem Silvas
 * @version 0.1.0
 */
@SpringBootApplication
@EnableMongoRepositories
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
