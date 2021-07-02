package com.dio.santander.apimanagerpoints.dtos;

import com.dio.santander.apimanagerpoints.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankOfHourDTO {
    @NotNull
    private BankOfHourPK id;
    @NotEmpty
    private String dateWorked;
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "9999999999.99")
    private BigDecimal amountOfHour;
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "9999999999.99")
    private BigDecimal balanceOfHour;
    @NotNull
    private UserCategory userCategory;
}
