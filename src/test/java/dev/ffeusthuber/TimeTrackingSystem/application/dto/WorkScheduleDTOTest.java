package dev.ffeusthuber.TimeTrackingSystem.application.dto;

import dev.ffeusthuber.TimeTrackingSystem.application.domain.model.employee.WorkSchedule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkScheduleDTOTest {

    @Test
    void workScheduleGetsConvertedToDTOCorrectly() {
        WorkSchedule workSchedule = WorkSchedule.createSpecificWorkSchedule(8.5f, 8.5f, 8.5f, 8.5f, 8.5f, 0, 0);

        WorkScheduleDTO workScheduleDTO = new WorkScheduleDTO(workSchedule);

        assertThat(workScheduleDTO.hoursMonday()).isEqualTo(8.5f);
        assertThat(workScheduleDTO.hoursTuesday()).isEqualTo(8.5f);
        assertThat(workScheduleDTO.hoursWednesday()).isEqualTo(8.5f);
        assertThat(workScheduleDTO.hoursThursday()).isEqualTo(8.5f);
        assertThat(workScheduleDTO.hoursFriday()).isEqualTo(8.5f);
        assertThat(workScheduleDTO.hoursSaturday()).isEqualTo(0);
        assertThat(workScheduleDTO.hoursSunday()).isEqualTo(0);
    }
}
