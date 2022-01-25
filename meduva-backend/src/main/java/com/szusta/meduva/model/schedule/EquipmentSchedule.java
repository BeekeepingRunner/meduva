package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.status.EquipmentStatus;
import com.szusta.meduva.payload.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_status_id")
    private EquipmentStatus equipmentStatus;

    public EquipmentSchedule(EquipmentItem equipmentItem, Date timeFrom, Date timeTo) {
        this.equipmentItem = equipmentItem;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }

    public EquipmentSchedule(EquipmentItem equipmentItem, TimeRange timeRange, EquipmentStatus eqStatus) {
        this.equipmentItem = equipmentItem;
        this.timeFrom = timeRange.getStartTime();
        this.timeTo = timeRange.getEndTime();
        this.equipmentStatus = eqStatus;
        this.deleted = false;
    }
}
