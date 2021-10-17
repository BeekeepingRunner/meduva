package com.szusta.meduva.repository.schedule.room;

import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomScheduleRepository extends UndeletableRepository<RoomSchedule> {

    @Query(
            value =
                    "select * from room_schedule s "
                            +   "where s.room_id = ?3 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<? super RoomSchedule> findAnyBetween(Date start, Date end, Long roomId);
}
