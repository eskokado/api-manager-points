package com.dio.santander.apimanagerpoints.models;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
public class BankOfHour {
    @EmbeddedId
    private BankOfHourPK id = new BankOfHourPK();
    @ManyToOne
    private UserCategory userCategory;
    private LocalDateTime dateWorked;
    private BigDecimal amountOfHour;
    private BigDecimal balanceOfHour;
}
