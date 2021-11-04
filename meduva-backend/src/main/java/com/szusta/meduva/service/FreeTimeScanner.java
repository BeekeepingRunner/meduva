package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.util.TimeUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Make sure that before using FreeTimeScanner, its fields are set appropriately
 */
@Component
@Scope("prototype")
@Setter
public class FreeTimeScanner {

    private User worker;
    private Service service;
    private List<Room> rooms;

    private ScheduleChecker scheduleChecker;

    @Autowired
    public FreeTimeScanner(ScheduleChecker scheduleChecker) {
        this.scheduleChecker = scheduleChecker;
    }

    public boolean isWorkerDayAvailable(Calendar day) {
        if (worker == null || service == null || rooms == null) {
            throw new RuntimeException("FreeTimeScanner not set appropriately, there are null fields");
        }

        Calendar currentTimeStamp =
                TimeUtils.getCalendar(TimeUtils.getDayStart(day.getTime()));

        Date potentialTermStart = currentTimeStamp.getTime();
        Date potentialTermEnd = getPotentialTermEnd(potentialTermStart, service.getDurationInMin());

        Date nextDayStart = TimeUtils.getNextDayStart(potentialTermStart);
        do {
            TimeRange potentialTermTimeRange = new TimeRange(potentialTermStart, potentialTermEnd);
            if (!scheduleChecker.isWorkerFree(potentialTermTimeRange, worker)) {
                return true;
            }

            currentTimeStamp.add(Calendar.MINUTE, 30);
            potentialTermStart = currentTimeStamp.getTime();
        } while (potentialTermStart.before(nextDayStart));

        return true;
    }

    private Date getPotentialTermEnd(Date potentialTermStart, int serviceDurationInMinutes) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(potentialTermStart);
        temp.add(Calendar.MINUTE, serviceDurationInMinutes);
        return temp.getTime();
    }
}
