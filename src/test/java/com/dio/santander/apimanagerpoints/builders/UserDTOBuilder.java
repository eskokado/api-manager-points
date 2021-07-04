package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.*;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public class UserDTOBuilder {
    @Builder.Default
    private long id = 1L;
    @Builder.Default
    private UserCategoryDTO userCategory = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
    @Builder.Default
    private String name = "User name 01";
    @Builder.Default
    private CompanyDTO company = CompanyDTOBuilder.builder().build().toCompanyDTO();
    @Builder.Default
    private AccessLevelDTO accessLevel = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
    @Builder.Default
    private WorkDayDTO workDay = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
    @Builder.Default
    private BigDecimal tolerance = BigDecimal.valueOf(5.5);
    @Builder.Default
    private String workDayStart = "2021-07-04T11:00:45";
    @Builder.Default
    private String workDayFinal = "2021-07-04T13:00:00";

    public UserDTO toUserDTO() {
        return new UserDTO(
          id, userCategory, name, company, accessLevel, workDay, tolerance, workDayStart, workDayFinal
        );
    }
}
