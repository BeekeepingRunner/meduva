package com.szusta.meduva.model.schedule;

import com.szusta.meduva.model.common.Undeletable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class Schedule extends Undeletable {

    protected String name;

    @Column(name = "time_from")
    protected Date timeFrom;
    @Column(name = "time_to")
    protected Date timeTo;

    protected String description;
}
