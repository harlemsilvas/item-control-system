package br.com.harlemsilvas.itemcontrol.core.domain.valueobject;

import java.util.Objects;

/**
 * Value Object que representa o timing de um alerta (quando deve ser disparado).
 */
public class AlertTiming {
    private final int value;
    private final String unit; // "KM", "DAYS", "HOURS", "MONTHS"

    public AlertTiming(int value, String unit) {
        if (value <= 0) {
            throw new IllegalArgumentException("Alert timing value must be positive");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Alert timing unit cannot be null or empty");
        }
        this.value = value;
        this.unit = unit.toUpperCase();
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    /**
     * Converte o timing para string legível.
     * Exemplo: "500 KM", "15 DAYS"
     */
    public String toReadableString() {
        return value + " " + unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertTiming that = (AlertTiming) o;
        return value == that.value && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "AlertTiming{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
