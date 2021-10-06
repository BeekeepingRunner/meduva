package com.szusta.meduva.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class Term {

    Date startTime;
    Date endTime;

    String serviceName;
    String workerName;
    String roomName;
    String eqItemName;

    public Term(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
