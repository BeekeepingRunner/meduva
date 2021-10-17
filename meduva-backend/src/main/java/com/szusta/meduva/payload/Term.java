package com.szusta.meduva.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Term {

    Date startTime;
    Date endTime;

    Long serviceId;
    Long workerId;
    Long clientId;
    Long roomId;
    Long eqItemId;

    public Term(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}