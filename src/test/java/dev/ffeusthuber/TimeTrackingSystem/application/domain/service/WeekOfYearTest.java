package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class WeekOfYearTest {

    @Test
    void validWeekOfYearReturnsCorrectWeekNumberAndYear() {
        WeekOfYear weekOfYear = new WeekOfYear(52, 2023);

        assertThat(weekOfYear.weekNumber()).isEqualTo(52);
        assertThat(weekOfYear.year()).isEqualTo(2023);
    }

    @Test
    void invalidWeekOfYearThrowsException() {
        assertThatThrownBy(() -> new WeekOfYear(0, 1992))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Week number must be between 1 and 53 for year 1992.");
    }

    @Test
    void tryingToGetWeekNumber53ForYearWith52WeeksThrowsException(){
        assertThatThrownBy(() -> new WeekOfYear(53, 2021))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Week number must be between 1 and 52 for year 2021.");
    }
}
