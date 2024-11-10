package dev.ffeusthuber.TimeTrackingSystem.application.domain.service;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.Employee;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.EmployeeRole;
import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;

public class WorkdayServiceTest {

    private static final long EMPLOYEE_ID = 1L;


//    @Test
//    void firstAddedClockInTimeEntryOnDateCreatesWorkdayForEmployee() {
//        ZonedDateTime day1ClockInTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
//        ZonedDateTime day2ClockInTime = ZonedDateTime.of(2021, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC);
//        Employee employee = createEmployee();
//        WorkdayService workdayService = new WorkdayService(
//                EmployeeRepositoryStub.withEmployee(employee),
//                WorkdayRepositoryStub.withoutWorkdays());
//
//        workdayService.addTimeEntryToWorkday(new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, day1ClockInTime));
//        workdayService.addTimeEntryToWorkday(new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_IN, day2ClockInTime));
//
//        assertThat(workdayService.getWorkdayForEmployeeOnDate(EMPLOYEE_ID,  day1ClockInTime.toLocalDate())).isNotEmpty();
//        assertThat(workdayService.getWorkdayForEmployeeOnDate(EMPLOYEE_ID,  day2ClockInTime.toLocalDate())).isNotEmpty();
//    }

//    @Test
//    void nonClockInTimeEntriesDoNotCreateAWorkdayButGetAddedToLatestWorkday() {
//        WorkdayRepository workdayRepository = WorkdayRepositoryStub.withWorkdays(new Workday(EMPLOYEE_ID, LocalDate.of(2020, 1, 1), 5.5f));
//        WorkdayService workdayService = new WorkdayService(null,workdayRepository);
//        TimeEntry clockOutTimeEntry = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, ZonedDateTime.now());
//
//        workdayService.addTimeEntryToWorkday(clockOutTimeEntry);
//
//        Optional<Workday> latestWorkday = workdayRepository.getLatestWorkdayForEmployee(EMPLOYEE_ID);
//
//        assertThat(latestWorkday).isPresent();
//        assertThat(latestWorkday.get().getTimeEntries()).contains(clockOutTimeEntry);
//    }

//    @Test
//    void throwsExceptionWhenAddingNonClockInTimeEntryWithoutExistingWorkday() {
//        WorkdayService workdayService = new WorkdayService(null, WorkdayRepositoryStub.withoutWorkdays());
//        TimeEntry clockOutTimeEntry = new TimeEntry(EMPLOYEE_ID, TimeEntryType.CLOCK_OUT, ZonedDateTime.now());
//
//        Assertions.assertThrows(IllegalStateException.class, () -> workdayService.addTimeEntryToWorkday(clockOutTimeEntry));
//    }

    private Employee createEmployee() {
        return new Employee(EMPLOYEE_ID, "Jane", "Doe", "j.doe@test-mail.com", "password", EmployeeRole.USER, WorkSchedule.createDefaultWorkSchedule());
    }
}
