package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.models.Calendar;
import com.dio.santander.apimanagerpoints.models.MovementPK;
import com.dio.santander.apimanagerpoints.models.Occurrence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
public class MovementDTOBuilder {
    @Builder.Default
    private MovementPK id = new MovementPK();
    @Builder.Default
    private String dateOfIn = "03-07-2021 11:20:15";
    @Builder.Default
    private String dateOfOut = "03-07-2021 13:20:15";
    @Builder.Default
    private BigDecimal period = BigDecimal.valueOf(3.5);
    @Builder.Default
    private OccurrenceDTO occurrence = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
    @Builder.Default
    private CalendarDTO calendar = CalendarDTOBuilder.builder().build().toCalendarDTO();

    public MovementDTO toMovementDTO() {
        id.setUserId(1);
        id.setMovementId(1);
        return new MovementDTO(
          id, dateOfIn, dateOfOut, period, occurrence, calendar
        );
    }
}
