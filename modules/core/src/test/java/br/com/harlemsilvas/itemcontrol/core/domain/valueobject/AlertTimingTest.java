package br.com.harlemsilvas.itemcontrol.core.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AlertTimingTest {

    @Test
    void shouldCreateAlertTimingSuccessfully() {
        // given
        int value = 500;
        String unit = "KM";

        // when
        AlertTiming timing = new AlertTiming(value, unit);

        // then
        assertThat(timing.getValue()).isEqualTo(500);
        assertThat(timing.getUnit()).isEqualTo("KM");
        assertThat(timing.toReadableString()).isEqualTo("500 KM");
    }

    @Test
    void shouldConvertUnitToUpperCase() {
        // when
        AlertTiming timing = new AlertTiming(15, "days");

        // then
        assertThat(timing.getUnit()).isEqualTo("DAYS");
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        // when / then
        assertThatThrownBy(() -> new AlertTiming(0, "KM"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Alert timing value must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // when / then
        assertThatThrownBy(() -> new AlertTiming(-10, "DAYS"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Alert timing value must be positive");
    }

    @Test
    void shouldThrowExceptionWhenUnitIsNull() {
        // when / then
        assertThatThrownBy(() -> new AlertTiming(500, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Alert timing unit cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenUnitIsEmpty() {
        // when / then
        assertThatThrownBy(() -> new AlertTiming(500, ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Alert timing unit cannot be null or empty");
    }

    @Test
    void shouldBeEqualWhenSameValueAndUnit() {
        // given
        AlertTiming timing1 = new AlertTiming(500, "KM");
        AlertTiming timing2 = new AlertTiming(500, "KM");

        // then
        assertThat(timing1).isEqualTo(timing2);
        assertThat(timing1.hashCode()).isEqualTo(timing2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        // given
        AlertTiming timing1 = new AlertTiming(500, "KM");
        AlertTiming timing2 = new AlertTiming(1000, "KM");

        // then
        assertThat(timing1).isNotEqualTo(timing2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentUnit() {
        // given
        AlertTiming timing1 = new AlertTiming(15, "DAYS");
        AlertTiming timing2 = new AlertTiming(15, "HOURS");

        // then
        assertThat(timing1).isNotEqualTo(timing2);
    }
}
