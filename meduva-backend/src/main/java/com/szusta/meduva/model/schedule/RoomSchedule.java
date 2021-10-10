package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.Room;
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
@Table(name = "room_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomSchedule extends Schedule {

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public RoomSchedule(Room room, Date timeFrom, Date timeTo) {
        this.room = room;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }
}
