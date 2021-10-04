package com.szusta.meduva.model.equipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.common.Activable;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "equipmentItem")
    @JsonIgnore
    private List<EquipmentSchedule> equipmentSchedules;

    public EquipmentItem(String name, boolean isActive, boolean deleted) {
        this.name = name;
        this.isActive = isActive;
        this.deleted = deleted;
    }
}
