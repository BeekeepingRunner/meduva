package com.szusta.meduva.repository;

import com.szusta.meduva.model.Service;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends UndeletableRepository<Service> {

    boolean existsByName(String name);
    Optional<Service> findByName(String name);
}
