package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import lombok.Builder;

@Builder
public class CalendarDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private DateTypeDTO dateType = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

    @Builder.Default
    private String description = "Calendar Description 01";

    @Builder.Default
    private String specialDate = "03-07-2021 09:30:40";

    public CalendarDTO toCalendarDTO() {
        return new CalendarDTO(id, dateType, description, specialDate);
    }
}
