package com.szusta.meduva.repository;

import com.szusta.meduva.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    boolean existsByName(String name);
    Optional<Service> findByName(String name);
}
