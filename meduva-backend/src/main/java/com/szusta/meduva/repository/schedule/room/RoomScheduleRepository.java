package com.szusta.meduva.repository.schedule.room;

import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomScheduleRepository extends UndeletableRepository<RoomSchedule> {

    @Query(
            value =
                    "select * from room_schedule rs "
                            +   "where rs.room_id = ?3 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) < 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<RoomSchedule> findAllDuring(Date startTime, Date endTime, long roomId);

    @Query(
            value =
                    "select * from room_schedule rs "
                            +   "where rs.room_id = ?3 and "
                            +   "rs.room_status_id = ?4 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) < 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<RoomSchedule> findAllDuring(Date startTime, Date endTime, long roomId, Long roomStatusId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM room_schedule rs WHERE rs.room_id = ?1 AND "
                    + " timestampdiff(MINUTE, time_from, ?1) <= 0 AND timestampdiff(MINUTE, time_to, ?2) >= 0"
    )
    void deleteByRoomIdBetween(Long roomId, Date start, Date end);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM room_schedule rs WHERE rs.room_id = ?1 AND "
                    + " rs.room_status_id = ?2 AND"
                    + " timestampdiff(MINUTE, time_from, ?3) <= 0 AND timestampdiff(MINUTE, time_to, ?4) >= 0"
    )
    void deleteByRoomIdBetween(Long roomId, Long roomStatusId, Date start, Date end);
}
