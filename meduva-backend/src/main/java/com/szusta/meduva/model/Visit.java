package com.szusta.meduva.model;

import com.szusta.meduva.model.common.Undeletable;
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
public class Visit extends Undeletable {

    @Column(name = "time_from")
    private Date timeFrom;
    @Column(name = "time_to")
    private Date timeTo;

    private String description;
    private boolean booked;
    private boolean cancelled = false;
    private boolean done = false;
    private boolean paid = false;

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
