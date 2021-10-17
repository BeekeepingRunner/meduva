package com.szusta.meduva.model.schedule.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "equipment_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentStatus extends EventStatus {

    @OneToMany(mappedBy = "equipmentStatus")
    @JsonIgnore
    private List<EquipmentSchedule> equipmentSchedules;
}
