package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import lombok.Builder;

@Builder
public class AccessLevelDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String description = "Access Level Description 01";

    public AccessLevelDTO toAccessLevelDTO() {
        return new AccessLevelDTO(id, description);
    }
}
