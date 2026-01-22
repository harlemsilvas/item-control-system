package br.com.harlemsilvas.itemcontrol.core.domain.valueobject;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ConditionOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object que representa uma condição de regra (pode conter múltiplas subcondições).
 */
public class RuleCondition {
    private final ConditionOperator operator;
    private final List<SubCondition> conditions;

    private RuleCondition(Builder builder) {
        this.operator = builder.operator;
        this.conditions = Collections.unmodifiableList(new ArrayList<>(builder.conditions));

        validate();
    }

    private void validate() {
        if (operator == null) {
            throw new IllegalArgumentException("Operator cannot be null");
        }

        // Se for operador lógico (OR, AND), deve ter pelo menos 2 subcondições
        if ((operator == ConditionOperator.OR || operator == ConditionOperator.AND)
                && (conditions == null || conditions.size() < 2)) {
            throw new IllegalArgumentException(
                "Logical operators (OR, AND) require at least 2 subconditions"
            );
        }

        // Se for operador simples, deve ter exatamente 1 subcondição
        if (operator != ConditionOperator.OR && operator != ConditionOperator.AND
                && (conditions == null || conditions.size() != 1)) {
            throw new IllegalArgumentException(
                "Simple operators require exactly 1 subcondition"
            );
        }
    }

    public ConditionOperator getOperator() {
        return operator;
    }

    public List<SubCondition> getConditions() {
        return conditions;
    }

    /**
     * Verifica se é uma condição composta (OR ou AND)
     */
    public boolean isComposite() {
        return operator == ConditionOperator.OR || operator == ConditionOperator.AND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleCondition that = (RuleCondition) o;
        return operator == that.operator &&
                Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, conditions);
    }

    @Override
    public String toString() {
        return "RuleCondition{" +
                "operator=" + operator +
                ", conditions=" + conditions +
                '}';
    }

    /**
     * Builder para RuleCondition
     */
    public static class Builder {
        private ConditionOperator operator;
        private List<SubCondition> conditions = new ArrayList<>();

        public Builder operator(ConditionOperator operator) {
            this.operator = operator;
            return this;
        }

        public Builder addCondition(SubCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        public Builder conditions(List<SubCondition> conditions) {
            this.conditions = new ArrayList<>(conditions);
            return this;
        }

        public RuleCondition build() {
            return new RuleCondition(this);
        }
    }
}
