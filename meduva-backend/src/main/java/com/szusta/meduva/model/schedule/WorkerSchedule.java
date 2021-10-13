package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.status.WorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "worker_status_id")
    private WorkerStatus workerStatus;

    public WorkerSchedule(User worker, Date timeFrom, Date timeTo) {
        this.user = worker;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.deleted = false;
    }
}
