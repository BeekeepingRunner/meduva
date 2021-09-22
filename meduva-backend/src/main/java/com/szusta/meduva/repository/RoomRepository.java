package com.szusta.meduva.repository;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends UndeletableWithNameRepository<Room> {

}
