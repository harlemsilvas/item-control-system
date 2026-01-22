package br.com.harlemsilvas.itemcontrol.core.domain.enums;

/**
 * Status possíveis de um alerta.
 */
public enum AlertStatus {
    /**
     * Alerta pendente, aguardando ação do usuário
     */
    PENDING,

    /**
     * Alerta foi lido pelo usuário
     */
    READ,

    /**
     * Alerta foi dispensado pelo usuário
     */
    DISMISSED,

    /**
     * Alerta foi marcado como resolvido
     */
    COMPLETED
}
