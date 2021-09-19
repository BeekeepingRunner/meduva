package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "equipment_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentItem extends Undeletable {

    private String name;

    @ManyToOne
    @JoinColumn(name = "equipment_model_id", nullable = false)
    @JsonIgnore
    private EquipmentModel equipmentModel;

    public EquipmentItem(String name, boolean deleted) {
        this.name = name;
        this.deleted = deleted;
    }
}
