package com.szusta.meduva.repository.schedule.room;

import com.szusta.meduva.model.schedule.status.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {

}
