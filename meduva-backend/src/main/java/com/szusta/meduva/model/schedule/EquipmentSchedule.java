package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.EquipmentItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "equipment_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSchedule extends Schedule {

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private EquipmentItem equipmentItem;
}
