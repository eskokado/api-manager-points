package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import lombok.Builder;

@Builder
public class WorkDayDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String description = "Work Day Description 01";

    public WorkDayDTO toWorkDayDTO() {
        return new WorkDayDTO(id, description);
    }
}
