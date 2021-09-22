package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service extends Undeletable {

    private String name;
    private String description;
    @Column(name = "duration_in_min")
    private int durationInMin;
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    Collection<EquipmentModel> equipmentModels;

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
