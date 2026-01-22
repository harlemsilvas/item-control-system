package br.com.harlemsilvas.itemcontrol.core.domain.enums;

/**
 * Status possíveis de um Item no sistema.
 */
public enum ItemStatus {
    /**
     * Item está ativo e sendo monitorado
     */
    ACTIVE,

    /**
     * Item está inativo temporariamente
     */
    INACTIVE,

    /**
     * Item foi arquivado e não está mais ativo
     */
    ARCHIVED
}
