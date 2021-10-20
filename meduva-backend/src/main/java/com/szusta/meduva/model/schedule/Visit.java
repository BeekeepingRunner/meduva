package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.AccountlessClient;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.status.VisitStatus;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visit_status_id")
    private VisitStatus visitStatus;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "unregistered_client_id")
    private AccountlessClient unregisteredClient;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_visit",
            joinColumns = @JoinColumn(name = "visit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "visit_equipment_item",
            joinColumns = @JoinColumn(name = "visit_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_item_id")
    )
    private List<EquipmentItem> eqItems = new ArrayList<>();

    public Visit(Date timeFrom, Date timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }
}
