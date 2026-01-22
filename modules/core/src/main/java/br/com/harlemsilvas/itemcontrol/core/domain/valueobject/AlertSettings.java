package br.com.harlemsilvas.itemcontrol.core.domain.valueobject;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.NotificationChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object que representa as configurações de um alerta.
 */
public class AlertSettings {
    private final List<AlertTiming> alertBefore;
    private final List<NotificationChannel> channels;
    private final int priority; // 1-5
    private final String customMessage;

    private AlertSettings(Builder builder) {
        this.alertBefore = Collections.unmodifiableList(new ArrayList<>(builder.alertBefore));
        this.channels = Collections.unmodifiableList(new ArrayList<>(builder.channels));
        this.priority = builder.priority;
        this.customMessage = builder.customMessage;

        validate();
    }

    private void validate() {
        if (alertBefore == null || alertBefore.isEmpty()) {
            throw new IllegalArgumentException("Alert settings must have at least one timing");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5");
        }
    }

    public List<AlertTiming> getAlertBefore() {
        return alertBefore;
    }

    public List<NotificationChannel> getChannels() {
        return channels;
    }

    public int getPriority() {
        return priority;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertSettings that = (AlertSettings) o;
        return priority == that.priority &&
                Objects.equals(alertBefore, that.alertBefore) &&
                Objects.equals(channels, that.channels) &&
                Objects.equals(customMessage, that.customMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertBefore, channels, priority, customMessage);
    }

    @Override
    public String toString() {
        return "AlertSettings{" +
                "alertBefore=" + alertBefore +
                ", channels=" + channels +
                ", priority=" + priority +
                ", customMessage='" + customMessage + '\'' +
                '}';
    }

    /**
     * Builder para AlertSettings
     */
    public static class Builder {
        private List<AlertTiming> alertBefore = new ArrayList<>();
        private List<NotificationChannel> channels = new ArrayList<>();
        private int priority = 3; // Default: prioridade média
        private String customMessage;

        public Builder addAlertTiming(AlertTiming timing) {
            this.alertBefore.add(timing);
            return this;
        }

        public Builder addAlertTiming(int value, String unit) {
            this.alertBefore.add(new AlertTiming(value, unit));
            return this;
        }

        public Builder addChannel(NotificationChannel channel) {
            this.channels.add(channel);
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder customMessage(String message) {
            this.customMessage = message;
            return this;
        }

        public AlertSettings build() {
            return new AlertSettings(this);
        }
    }
}
