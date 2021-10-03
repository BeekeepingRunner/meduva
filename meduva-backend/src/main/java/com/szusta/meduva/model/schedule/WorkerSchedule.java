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

@Entity
@Table(name = "worker_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerSchedule extends Schedule {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
