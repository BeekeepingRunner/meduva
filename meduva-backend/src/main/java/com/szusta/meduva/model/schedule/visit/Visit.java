package com.szusta.meduva.model.schedule.visit;

import com.szusta.meduva.model.AccountlessClient;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.Schedule;
import com.szusta.meduva.model.schedule.status.VisitStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "visit")
@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<UserVisit> userVisits;

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

    public Visit(Date timeFrom, Date timeTo, UserVisit... userVisits) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        for(UserVisit userVisit : userVisits)
            userVisit.setVisit(this);
        this.userVisits = Stream.of(userVisits).collect(Collectors.toList());
    }
}
