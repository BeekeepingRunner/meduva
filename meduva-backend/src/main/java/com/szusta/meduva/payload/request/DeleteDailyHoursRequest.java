package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DeleteDailyHoursRequest {

    private Date Day;
}
