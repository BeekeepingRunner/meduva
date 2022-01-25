package com.szusta.meduva.model.schedule.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "worker_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerStatus extends EventStatus {

    @OneToMany(mappedBy = "workerStatus")
    @JsonIgnore
    private List<WorkerSchedule> workerSchedules;
}
