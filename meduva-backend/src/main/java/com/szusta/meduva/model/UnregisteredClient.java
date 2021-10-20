package com.szusta.meduva.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szusta.meduva.model.common.Undeletable;
import com.szusta.meduva.model.schedule.Visit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "unregistered_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnregisteredClient extends Undeletable {

    private String name;
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "unregisteredClient")
    @JsonIgnore
    List<Visit> visits;
}
