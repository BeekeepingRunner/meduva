package com.szusta.meduva.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WeekBoundaries {

    private Date firstWeekDay;
    private Date lastWeekDay;

}
