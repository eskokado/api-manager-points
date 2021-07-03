package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import lombok.Builder;

@Builder
public class DateTypeDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String description = "Date Type Description 01";

    public DateTypeDTO toDateTypeDTO(){
        return new DateTypeDTO(id, description);
    }
}
