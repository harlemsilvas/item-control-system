package br.com.harlemsilvas.itemcontrol.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Aplicação principal do Worker do Item Control System.
 * Responsável pelo processamento em background, scheduler e motor de regras.
 * 
 * @author Harlem Silvas
 * @version 0.1.0
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
