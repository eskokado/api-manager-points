package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.UserDTO;
import com.dio.santander.apimanagerpoints.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "workDayStart", source = "workDayStart", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "workDayFinal", source = "workDayFinal", dateFormat = "dd-MM-yyyy HH:mm:ss")
    User toModel(UserDTO userDTO);

    UserDTO toDto(User user);
}
