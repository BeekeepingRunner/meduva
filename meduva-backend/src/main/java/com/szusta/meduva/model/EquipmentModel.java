package com.szusta.meduva.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "equipment_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentModel extends Undeletable {

    private String name;

    public EquipmentModel(String name, boolean deleted) {
        this.name = name;
        this.deleted = deleted;
    }
}
