package com.szusta.meduva.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TimeRange {

    private Date startTime;
    private Date endTime;

    public TimeRange(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
