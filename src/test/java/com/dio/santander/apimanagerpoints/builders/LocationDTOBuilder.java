package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import lombok.Builder;

@Builder
public class LocationDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private AccessLevelDTO accessLevel = new AccessLevelDTO();

    @Builder.Default
    private String description = "Location Description 01";

    public LocationDTO toLocationDTO(){
        return new LocationDTO(id, accessLevel, description);
    }
}
