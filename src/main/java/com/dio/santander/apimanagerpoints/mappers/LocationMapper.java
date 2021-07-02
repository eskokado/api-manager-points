package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import com.dio.santander.apimanagerpoints.models.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toModel(LocationDTO locationDTO);

    LocationDTO toDto(Location location);
}
