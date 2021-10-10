package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.equipment.EquipmentItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "equipment_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSchedule extends Schedule {

    @ManyToOne
    @JoinColumn(name = "equipment_item_id")
    private EquipmentItem equipmentItem;

    public EquipmentSchedule(EquipmentItem equipmentItem, Date timeFrom, Date timeTo) {
        this.equipmentItem = equipmentItem;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }
}
