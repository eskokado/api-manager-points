package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import lombok.Builder;

@Builder
public class OccurrenceDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String name = "Occurrence Name 01";

    @Builder.Default
    private String description = "Occurrence Description 01";

    public OccurrenceDTO toOccurrenceDTO() {
        return new OccurrenceDTO(id, name, description);
    }
}
