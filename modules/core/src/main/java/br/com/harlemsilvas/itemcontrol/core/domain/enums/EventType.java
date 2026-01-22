package br.com.harlemsilvas.itemcontrol.core.domain.enums;

/**
 * Tipos de eventos que podem ser registrados para um Item.
 */
public enum EventType {
    /**
     * Manutenção realizada (ex: troca de óleo, revisão)
     */
    MAINTENANCE,

    /**
     * Pagamento efetuado (ex: conta paga)
     */
    PAYMENT,

    /**
     * Medição/leitura (ex: km, m³, litros)
     */
    MEASUREMENT,

    /**
     * Compra de item consumível (ex: galão de água)
     */
    PURCHASE,

    /**
     * Atualização de dados do item
     */
    UPDATE
}
