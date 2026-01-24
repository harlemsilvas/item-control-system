package br.com.harlemsilvas.itemcontrol.worker.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Scheduler para processamento automatico de regras.
 * Executa periodicamente o Rules Engine para gerar alertas.
 *
 * NOTA: Temporariamente desabilitado ate implementacao completa do ProcessRulesUseCase
 *
 * @author Harlem Silva
 * @since 2026-01-23
 */
@Component
public class RuleProcessorScheduler {

    private static final Logger log = LoggerFactory.getLogger(RuleProcessorScheduler.class);

    public RuleProcessorScheduler() {
        log.info("RuleProcessorScheduler inicializado (funcionalidade temporariamente desabilitada)");
    }
}
