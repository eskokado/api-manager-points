package com.dio.santander.apimanagerpoints.mappers;

import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.models.UserCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserCategoryMapper {
    UserCategoryMapper INSTANCE = Mappers.getMapper(UserCategoryMapper.class);

    UserCategory toModel(UserCategoryDTO userCategoryDTO);

    UserCategoryDTO toDto(UserCategory userCategory);
}
