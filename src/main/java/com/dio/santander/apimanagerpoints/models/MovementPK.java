package com.dio.santander.apimanagerpoints.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class MovementPK implements Serializable {
    private static final long serialVersionUID = 1L;

    private long movementId;
    private long userId;
}
