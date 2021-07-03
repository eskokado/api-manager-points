package com.dio.santander.apimanagerpoints.dtos;

import com.dio.santander.apimanagerpoints.models.MovementPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {
    @NotNull
    private MovementPK id;
    @NotEmpty
    private String dateOfIn;
    @NotEmpty
    private String dateOfOut;
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.0")
    private BigDecimal period;
    @NotNull
    private OccurrenceDTO occurrence;
    @NotNull
    private CalendarDTO calendar;
}
