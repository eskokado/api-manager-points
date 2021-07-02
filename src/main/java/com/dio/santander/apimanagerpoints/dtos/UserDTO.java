package com.dio.santander.apimanagerpoints.dtos;

import com.dio.santander.apimanagerpoints.models.AccessLevel;
import com.dio.santander.apimanagerpoints.models.Company;
import com.dio.santander.apimanagerpoints.models.UserCategory;
import com.dio.santander.apimanagerpoints.models.WorkDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull
    private long id;
    @NotNull
    private UserCategory userCategory;
    @NotEmpty
    private String name;
    @NotNull
    private Company company;
    @NotNull
    private AccessLevel accessLevel;
    @NotNull
    private WorkDay workDay;
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.0")
    private BigDecimal tolerance;
    @NotEmpty
    private String workDayStart;
    @NotEmpty
    private String workDayFinal;
}
