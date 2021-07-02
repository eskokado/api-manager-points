package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.models.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovementMapper {
    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

    @Mapping(target = "dateOfIn", source = "dateOfIn", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "dateOfOut", source = "dateOfOut", dateFormat = "dd-MM-yyyy HH:mm:ss")
    Movement toModel(MovementDTO movementDTO);

    MovementDTO toDto(Movement movement);
}
