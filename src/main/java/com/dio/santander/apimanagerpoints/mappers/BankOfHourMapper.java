package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.models.BankOfHour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankOfHourMapper {
    BankOfHourMapper INSTANCE = Mappers.getMapper(BankOfHourMapper.class);

//    @Mapping(target = "dateWorked", source = "dateWorked", dateFormat = "dd-MM-yyyy HH:mm:ss")
    BankOfHour toModel(BankOfHourDTO bankOfHourDTO);

    BankOfHourDTO toDto(BankOfHour bankOfHour);
}
