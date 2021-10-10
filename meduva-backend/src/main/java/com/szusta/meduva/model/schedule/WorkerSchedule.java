package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.User;
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
@Table(name = "worker_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSchedule extends Schedule {

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private User user;

    public WorkerSchedule(User worker, Date timeFrom, Date timeTo) {
        this.user = worker;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }
}
