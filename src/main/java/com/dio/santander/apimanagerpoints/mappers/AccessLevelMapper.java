package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.models.AccessLevel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccessLevelMapper {
    AccessLevelMapper INSTANCE = Mappers.getMapper(AccessLevelMapper.class);

    AccessLevel toModel(AccessLevelDTO accessLevelDTO);

    AccessLevelDTO toDto(AccessLevel accessLevel);
}
