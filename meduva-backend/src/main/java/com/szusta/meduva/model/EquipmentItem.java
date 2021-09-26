package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.common.Activable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "equipment_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentItem extends Activable {

    private String name;

    @ManyToOne
    @JoinColumn(name = "equipment_model_id")
    @JsonIgnore
    private EquipmentModel equipmentModel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public EquipmentItem(String name, boolean isActive, boolean deleted) {
        this.name = name;
        this.isActive = isActive;
        this.deleted = deleted;
    }
}
