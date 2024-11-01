package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class WorkdayRepositoryStub implements WorkdayRepository {
    private final List<Workday> workdayList = new ArrayList<>();

    private WorkdayRepositoryStub(Workday... workdays) {
        workdayList.addAll(Arrays.asList(workdays));
    }

    public static WorkdayRepositoryStub withWorkdays(Workday... workdays) {
        return new WorkdayRepositoryStub(workdays);
    }

    public static WorkdayRepository withoutWorkdays() {
        return new WorkdayRepositoryStub();
    }

    @Override
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date, ZoneId zoneId) {
        return workdayList.stream()
                          .filter(workday -> workday.getEmployeeId() == employeeID && workday.getWorkDate().equals(date))
                          .findFirst();
    }

    @Override
    public void saveWorkday(Workday workday) {
        workdayList.add(workday);
    }

    @Override
    public Optional<Workday> getLatestWorkdayForEmployee(long employeeID) {
        return workdayList.stream()
                          .filter(workday -> workday.getEmployeeId() == employeeID)
                          .max(Comparator.comparing(Workday::getWorkDate));
    }
}
