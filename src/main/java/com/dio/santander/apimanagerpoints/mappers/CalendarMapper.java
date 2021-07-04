package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.models.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CalendarMapper {
    CalendarMapper INSTANCE = Mappers.getMapper(CalendarMapper.class);

//    @Mapping(target = "specialDate", source = "specialDate", dateFormat = "yyyy-MM-ddTHH:mm:ss")
    Calendar toModel(CalendarDTO calendarDTO);

    CalendarDTO toDto(Calendar calendar);
}
