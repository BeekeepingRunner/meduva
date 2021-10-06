package com.szusta.meduva.repository;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends UndeletableWithNameRepository<Room> {

    @Query(
            value = "SELECT * FROM room r "
                    + "INNER JOIN room_service rs ON rs.room_id = r.id "
                    + "INNER JOIN service s ON s.id = rs.service_id "
                    + "WHERE s.id = ?1",
            nativeQuery = true
    )
    List<Room> findAllSuitableForService(Long serviceId);
}
