package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.models.Occurrence;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OccurrenceMapper {
    OccurrenceMapper INSTANCE = Mappers.getMapper(OccurrenceMapper.class);

    Occurrence toModel(OccurrenceDTO occurrenceDTO);

    OccurrenceDTO toDto(Occurrence occurrence);
}
