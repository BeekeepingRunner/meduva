package com.szusta.meduva.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OffWorkHours {

    private Date startTime;
    private Date endTime;
}
