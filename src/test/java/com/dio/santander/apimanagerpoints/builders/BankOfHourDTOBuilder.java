package com.dio.santander.apimanagerpoints.builders;

import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.models.BankOfHourPK;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public class BankOfHourDTOBuilder {
    @Builder.Default
    private BankOfHourPK id = new BankOfHourPK();
    @Builder.Default
    private String dateWorked = "2021-07-04T13:00:00";
    @Builder.Default
    private BigDecimal amountOfHour = BigDecimal.valueOf(1500.99);
    @Builder.Default
    private BigDecimal balanceOfHour = BigDecimal.valueOf(3000.00);
    @Builder.Default
    private UserCategoryDTO userCategory = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

    public BankOfHourDTO toBankOfHourDTO() {
        id.setBankOfHourId(1);
        id.setMovementId(1);
        id.setUserId(1);
        return new BankOfHourDTO(
                id, dateWorked, amountOfHour, balanceOfHour, userCategory
        );
    }
}
