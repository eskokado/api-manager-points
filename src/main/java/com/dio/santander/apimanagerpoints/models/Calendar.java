package com.dio.santander.apimanagerpoints.models;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Audited
public class Calendar {
    @Id
    private Long id;
    @ManyToOne
    private DateType dateType;
    private String description;
    private LocalDateTime specialDate;
}
