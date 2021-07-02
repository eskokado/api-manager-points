package com.dio.santander.apimanagerpoints.models;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Audited
public class BankOfHour implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private BankOfHourPK id = new BankOfHourPK();
    @ManyToOne
    private UserCategory userCategory;
    private LocalDateTime dateWorked;
    private BigDecimal amountOfHour;
    private BigDecimal balanceOfHour;
}
