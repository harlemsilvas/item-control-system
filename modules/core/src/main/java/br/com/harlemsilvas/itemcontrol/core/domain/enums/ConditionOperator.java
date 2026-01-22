package br.com.harlemsilvas.itemcontrol.core.domain.enums;

/**
 * Operadores lógicos e de comparação para condições de regras.
 */
public enum ConditionOperator {
    /**
     * Igualdade (=)
     */
    EQUALS,

    /**
     * Maior que (>)
     */
    GREATER_THAN,

    /**
     * Menor que (<)
     */
    LESS_THAN,

    /**
     * Maior ou igual (>=)
     */
    GREATER_THAN_OR_EQUAL,

    /**
     * Menor ou igual (<=)
     */
    LESS_THAN_OR_EQUAL,

    /**
     * Intervalo/diferença desde último valor
     */
    INTERVAL,

    /**
     * Operador lógico OR
     */
    OR,

    /**
     * Operador lógico AND
     */
    AND
}
