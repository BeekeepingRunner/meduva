package com.szusta.meduva.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WorkHoursPayload {

    private Date startTime;
    private Date endTime;
}
