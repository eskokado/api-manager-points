package com.dio.santander.apimanagerpoints.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class BankOfHourPK implements Serializable {
    private long bankOfHourId;
    private long movementId;
    private long userId;
}
