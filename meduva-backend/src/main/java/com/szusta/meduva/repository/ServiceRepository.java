package com.szusta.meduva.repository;

import com.szusta.meduva.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    public boolean existsByName(String name);
}
