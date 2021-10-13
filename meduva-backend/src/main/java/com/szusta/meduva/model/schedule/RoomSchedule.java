package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.schedule.status.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_status_id")
    private RoomStatus roomStatus;

    public RoomSchedule(Room room, Date timeFrom, Date timeTo) {
        this.room = room;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }
}
