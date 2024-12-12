package dev.ffeusthuber.TimeTrackingSystem.adapter.out;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.workday.Workday;
import dev.ffeusthuber.TimeTrackingSystem.application.port.out.WorkdayRepository;

import java.time.LocalDate;
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
    public Optional<Workday> getWorkdayForEmployeeOnDate(long employeeID, LocalDate date) {
        return workdayList.stream()
                          .filter(workday -> workday.getEmployeeId() == employeeID && workday.getWorkDate().equals(date))
                          .findFirst();
    }

    @Override
    public List<Workday> getAllWorkdaysOfEmployee(long employeeId) {
        return workdayList.stream().filter(workday -> workday.getEmployeeId() == employeeId).toList();
    }

    @Override
    public void deleteAllWorkdaysOfEmployee(long employeeId) {
        workdayList.removeAll(
                getAllWorkdaysOfEmployee(employeeId));
    }

    @Override
    public List<Workday> getWorkdaysForEmployeeBetweenDates(long employeeId, LocalDate fromIncluding, LocalDate toIncluding) {
        return workdayList.stream().filter(workday -> workday.getEmployeeId() == employeeId &&
                                                     !workday.getWorkDate().isBefore(fromIncluding) &&
                                                     !workday.getWorkDate().isAfter(toIncluding))
                          .toList();
    }

    @Override
    public Workday save(Workday workday) {
        workdayList.add(workday);
        return workday;
    }

    @Override
    public Optional<Workday> getLatestWorkdayForEmployee(long employeeID) {
        return workdayList.stream()
                          .filter(workday -> workday.getEmployeeId() == employeeID)
                          .max(Comparator.comparing(Workday::getWorkDate));
    }
}
