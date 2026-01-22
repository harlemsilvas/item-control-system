package br.com.harlemsilvas.itemcontrol.worker.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuração do scheduler do worker.
 * 
 * O scheduler só é ativado se a propriedade scheduler.enabled=true.
 * Isso permite controlar via profiles (dev, prod) quando o worker deve rodar.
 * 
 * @author Harlem Silvas
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(
    name = "scheduler.enabled", 
    havingValue = "true", 
    matchIfMissing = false
)
public class SchedulerConfig {
    // Configuração habilitada via propriedade
}
