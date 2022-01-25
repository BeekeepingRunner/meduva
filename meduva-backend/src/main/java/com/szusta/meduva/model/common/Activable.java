package com.szusta.meduva.model.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class Activable extends ScheduleSubject {

    @Column(name = "is_active")
    protected boolean isActive;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
