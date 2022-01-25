package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.common.ScheduleSubject;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.RoomSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends ScheduleSubject {

    private String name;
    private String description;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<EquipmentItem> equipmentItems;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "room_service",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<RoomSchedule> roomSchedules;

    public Room(String name,
                String description,
                boolean deleted) {
        this.name = name;
        this.description = description;
        this.deleted = deleted;
    }
}
