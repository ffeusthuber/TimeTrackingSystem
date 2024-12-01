package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

public record WeekOfYear(int weekNumber, int year) {
    public WeekOfYear {
        validateWeekNumber(weekNumber, year);
    }

    private void validateWeekNumber(int weekNumber, int year) {
        int numberOfWeeksInYear = getNumberOfWeeksInYear(year);
        if (weekNumber < 1 || weekNumber > numberOfWeeksInYear) {
            throw new IllegalArgumentException("Week number must be between 1 and " + numberOfWeeksInYear + " for year " + year + ".");
        }
    }

    private int getNumberOfWeeksInYear(int year) {
        LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
        int lastWeekOfYear = lastDayOfYear.get(WeekFields.ISO.weekOfWeekBasedYear());
        //if the last day of the year falls in the first week of the next year
        if (lastWeekOfYear == 1) {
            return lastDayOfYear.minusWeeks(1).get(WeekFields.ISO.weekOfWeekBasedYear());
        }
        return lastWeekOfYear;
    }
}
