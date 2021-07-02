package com.dio.santander.apimanagerpoints.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {
    @NotNull
    private long id;

    @NotNull
    private DateTypeDTO dateType;

    @NotEmpty
    private String description;

    private String specialDate;
}
