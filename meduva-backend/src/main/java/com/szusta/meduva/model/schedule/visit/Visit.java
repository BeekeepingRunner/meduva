package com.szusta.meduva.model.schedule.visit;

import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "visit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Visit extends Schedule {

    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "visit_status_id")
    private VisitStatus visitStatus;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_visit",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "visit_id")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "visit_equipment_item",
            joinColumns = @JoinColumn(name = "equipment_item_id"),
            inverseJoinColumns = @JoinColumn(name = "visit_id")
    )
    private List<EquipmentItem> eqItems = new ArrayList<>();

    public Visit(Date timeFrom, Date timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }
}
