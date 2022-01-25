package com.szusta.meduva.model.schedule.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.schedule.RoomSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "room_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatus extends EventStatus {

    @OneToMany(mappedBy = "roomStatus")
    @JsonIgnore
    private List<RoomSchedule> roomSchedules;
}
