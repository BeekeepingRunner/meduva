package com.szusta.meduva.model.schedule.visit;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class VisitStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "visitStatus")
    @JsonIgnore
    private List<Visit> visits;
}
