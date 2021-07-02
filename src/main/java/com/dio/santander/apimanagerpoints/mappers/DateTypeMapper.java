package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import com.dio.santander.apimanagerpoints.models.DateType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DateTypeMapper {
    DateTypeMapper INSTANCE = Mappers.getMapper(DateTypeMapper.class);

    DateType toModel(DateTypeDTO dateTypeDTO);

    DateTypeDTO toDto(DateType dateType);
}
