package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorkdayRepositoryStub implements WorkdayRepository {
    private final List<Workday> workdayList = new ArrayList<>();

    private WorkdayRepositoryStub(Workday... workdays) {
        workdayList.addAll(Arrays.asList(workdays));
    }

    public static WorkdayRepositoryStub withWorkday(Workday workday) {
        return new WorkdayRepositoryStub(workday);
    }

    public static WorkdayRepository withoutWorkdays() {
        return new WorkdayRepositoryStub();
    }

    @Override
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, ZonedDateTime date) {
        return workdayList.stream()
                          .filter(workday -> workday.getEmployeeId() == employeeID && workday.getWorkDate().toLocalDate().equals(date.toLocalDate()))
                          .findFirst();
    }
}
