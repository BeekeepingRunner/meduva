package com.szusta.meduva.model.schedule.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.schedule.Visit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "visit_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitStatus extends EventStatus {

    @OneToMany(mappedBy = "visitStatus")
    @JsonIgnore
    private List<Visit> visits;
}
