package com.szusta.meduva.repository;

import com.szusta.meduva.model.Room;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends UndeletableRepository<Room> {

    boolean existsByName(String name);
    Optional<Room> findByName(String name);
}
