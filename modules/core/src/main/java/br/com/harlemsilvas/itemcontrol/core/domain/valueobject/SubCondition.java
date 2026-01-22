package br.com.harlemsilvas.itemcontrol.core.domain.valueobject;

import java.util.Objects;

/**
 * Value Object que representa uma subcondição de regra.
 */
public class SubCondition {
    private final String metric;        // "km", "time", "consumption"
    private final Number threshold;     // Valor limite
    private final String unit;          // "MONTHS", "DAYS", "KM", etc (opcional)
    private final String operator;      // ">=", "<=", "INTERVAL", etc
    private final String baseMetric;    // Campo de referência (ex: "lastOilChangeKm")

    private SubCondition(Builder builder) {
        this.metric = builder.metric;
        this.threshold = builder.threshold;
        this.unit = builder.unit;
        this.operator = builder.operator;
        this.baseMetric = builder.baseMetric;

        validate();
    }

    private void validate() {
        if (metric == null || metric.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric cannot be null or empty");
        }
        if (threshold == null) {
            throw new IllegalArgumentException("Threshold cannot be null");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be null or empty");
        }
    }

    public String getMetric() {
        return metric;
    }

    public Number getThreshold() {
        return threshold;
    }

    public String getUnit() {
        return unit;
    }

    public String getOperator() {
        return operator;
    }

    public String getBaseMetric() {
        return baseMetric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCondition that = (SubCondition) o;
        return Objects.equals(metric, that.metric) &&
                Objects.equals(threshold, that.threshold) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(baseMetric, that.baseMetric);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metric, threshold, unit, operator, baseMetric);
    }

    @Override
    public String toString() {
        return "SubCondition{" +
                "metric='" + metric + '\'' +
                ", threshold=" + threshold +
                ", unit='" + unit + '\'' +
                ", operator='" + operator + '\'' +
                ", baseMetric='" + baseMetric + '\'' +
                '}';
    }

    /**
     * Builder para SubCondition
     */
    public static class Builder {
        private String metric;
        private Number threshold;
        private String unit;
        private String operator;
        private String baseMetric;

        public Builder metric(String metric) {
            this.metric = metric;
            return this;
        }

        public Builder threshold(Number threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder baseMetric(String baseMetric) {
            this.baseMetric = baseMetric;
            return this;
        }

        public SubCondition build() {
            return new SubCondition(this);
        }
    }
}
