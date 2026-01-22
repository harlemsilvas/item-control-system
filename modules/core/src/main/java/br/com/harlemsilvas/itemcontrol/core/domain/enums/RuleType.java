package br.com.harlemsilvas.itemcontrol.core.domain.enums;

/**
 * Tipos de regras de alerta suportados pelo sistema.
 */
public enum RuleType {
    /**
     * Regra baseada em tempo/data (ex: vencimento mensal)
     */
    TIME_BASED,

    /**
     * Regra baseada em métrica numérica (ex: quilometragem)
     */
    METRIC_BASED,

    /**
     * Regra composta com múltiplas condições (OR, AND)
     */
    COMPOSITE,

    /**
     * Regra baseada em consumo médio com previsão
     */
    CONSUMPTION_BASED
}
