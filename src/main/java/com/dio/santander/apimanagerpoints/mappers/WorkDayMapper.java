package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.models.WorkDay;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkDayMapper {
    WorkDayMapper INSTANCE = Mappers.getMapper(WorkDayMapper.class);

    WorkDay toModel(WorkDayDTO workDayDTO);

    WorkDayDTO toDto(WorkDay workDay);
}
