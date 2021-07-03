package com.dio.santander.apimanagerpoints.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull
    private long id;
    @NotNull
    private UserCategoryDTO userCategory;
    @NotEmpty
    private String name;
    @NotNull
    private CompanyDTO company;
    @NotNull
    private AccessLevelDTO accessLevel;
    @NotNull
    private WorkDayDTO workDay;
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.0")
    private BigDecimal tolerance;
    @NotEmpty
    private String workDayStart;
    @NotEmpty
    private String workDayFinal;
}
