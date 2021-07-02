package com.dio.santander.apimanagerpoints.dtos;

import com.dio.santander.apimanagerpoints.models.Calendar;
import com.dio.santander.apimanagerpoints.models.MovementPK;
import com.dio.santander.apimanagerpoints.models.Occurrence;
import com.dio.santander.apimanagerpoints.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Occurrence occurrence;
    @NotNull
    private Calendar calendar;
}
