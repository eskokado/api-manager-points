package com.dio.santander.apimanagerpoints.models;

import lombok.*;
import org.hibernate.envers.Audited;

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
@Audited
public class Movement {
    @EmbeddedId
    private MovementPK id = new MovementPK();

    private LocalDateTime dateOfIn;
    private LocalDateTime dateOfOut;
    private BigDecimal period;
    @ManyToOne
    private Occurrence occurrence;
    @ManyToOne
    private Calendar calendar;
}
