package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertStatus;
import br.com.harlemsilvas.itemcontrol.core.domain.enums.AlertType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AlertTest {

    @Test
    void shouldCreateAlertWithBuilder() {
        // given
        UUID ruleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // when
        Alert alert = new Alert.Builder()
            .ruleId(ruleId)
            .itemId(itemId)
            .userId(userId)
            .alertType(AlertType.WARNING)
            .title("Troca de Óleo Próxima")
            .message("Faltam 450 km para próxima troca de óleo")
            .priority(3)
            .build();

        // then
        assertThat(alert.getId()).isNotNull();
        assertThat(alert.getRuleId()).isEqualTo(ruleId);
        assertThat(alert.getItemId()).isEqualTo(itemId);
        assertThat(alert.getUserId()).isEqualTo(userId);
        assertThat(alert.getAlertType()).isEqualTo(AlertType.WARNING);
        assertThat(alert.getTitle()).isEqualTo("Troca de Óleo Próxima");
        assertThat(alert.getMessage()).isEqualTo("Faltam 450 km para próxima troca de óleo");
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.PENDING);
        assertThat(alert.getPriority()).isEqualTo(3);
        assertThat(alert.isPending()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenRuleIdIsNull() {
        // when / then
        assertThatThrownBy(() -> new Alert.Builder()
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Test")
            .message("Test message")
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("RuleId cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenPriorityIsInvalid() {
        // when / then
        assertThatThrownBy(() -> new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Test")
            .message("Test message")
            .priority(6)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Priority must be between 1 and 5");
    }

    @Test
    void shouldMarkAsRead() {
        // given
        Alert alert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Test")
            .message("Test message")
            .build();

        // when
        alert.markAsRead();

        // then
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.READ);
        assertThat(alert.getReadAt()).isNotNull();
        assertThat(alert.isPending()).isFalse();
    }

    @Test
    void shouldNotMarkAsReadWhenAlreadyRead() {
        // given
        Alert alert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Test")
            .message("Test message")
            .status(AlertStatus.READ)
            .build();

        Instant firstReadAt = alert.getReadAt();

        // when
        alert.markAsRead();

        // then
        assertThat(alert.getReadAt()).isEqualTo(firstReadAt);
    }

    @Test
    void shouldDismissAlert() {
        // given
        Alert alert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Test")
            .message("Test message")
            .build();

        // when
        alert.dismiss();

        // then
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.DISMISSED);
    }

    @Test
    void shouldCompleteAlert() {
        // given
        Alert alert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Test")
            .message("Test message")
            .build();

        // when
        alert.complete();

        // then
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.COMPLETED);
        assertThat(alert.getCompletedAt()).isNotNull();
    }

    @Test
    void shouldIdentifyUrgentAlert() {
        // given
        Alert urgentAlert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.URGENT)
            .title("Test")
            .message("Test message")
            .build();

        Alert normalAlert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Test")
            .message("Test message")
            .build();

        // then
        assertThat(urgentAlert.isUrgent()).isTrue();
        assertThat(normalAlert.isUrgent()).isFalse();
    }

    @Test
    void shouldIdentifyOverdueAlert() {
        // given
        Instant pastDate = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant futureDate = Instant.now().plus(1, ChronoUnit.DAYS);

        Alert overdueAlert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Test")
            .message("Test message")
            .dueAt(pastDate)
            .build();

        Alert notOverdueAlert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Test")
            .message("Test message")
            .dueAt(futureDate)
            .build();

        Alert noDueAlert = new Alert.Builder()
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Test")
            .message("Test message")
            .build();

        // then
        assertThat(overdueAlert.isOverdue()).isTrue();
        assertThat(notOverdueAlert.isOverdue()).isFalse();
        assertThat(noDueAlert.isOverdue()).isFalse();
    }

    @Test
    void shouldBeEqualWhenSameId() {
        // given
        UUID id = UUID.randomUUID();

        Alert alert1 = new Alert.Builder()
            .id(id)
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.INFO)
            .title("Alert 1")
            .message("Message 1")
            .build();

        Alert alert2 = new Alert.Builder()
            .id(id)
            .ruleId(UUID.randomUUID())
            .itemId(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .alertType(AlertType.WARNING)
            .title("Alert 2")
            .message("Message 2")
            .build();

        // then
        assertThat(alert1).isEqualTo(alert2);
        assertThat(alert1.hashCode()).isEqualTo(alert2.hashCode());
    }
}
