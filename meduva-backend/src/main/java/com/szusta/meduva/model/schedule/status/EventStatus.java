package com.szusta.meduva.model.schedule.status;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class EventStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
}
