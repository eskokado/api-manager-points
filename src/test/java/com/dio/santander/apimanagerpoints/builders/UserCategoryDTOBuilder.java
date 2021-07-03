package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import lombok.Builder;

@Builder
public class UserCategoryDTOBuilder {
    @Builder.Default
    private long id = 1L;

    @Builder.Default
    private String description = "User Category Description 01";

    public UserCategoryDTO toUserCategoryDTO() {
        return new UserCategoryDTO(id, description);
    }
}
