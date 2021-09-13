package com.szusta.meduva.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "service",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Column(name = "duration_in_min")
    private int durationInMin;
    private BigDecimal price;
    private boolean deleted;

    public Service(String name,
                   String description,
                   int durationInMin,
                   BigDecimal price,
                   boolean deleted) {
        this.name = name;
        this.description = description;
        this.durationInMin = durationInMin;
        this.price = price;
        this.deleted = deleted;
    }
}
