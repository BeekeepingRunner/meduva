package com.szusta.meduva.repository;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends UndeletableWithNameRepository<Service> {

    List<Service> findByItemlessTrue();
    List<Service> findByItemlessFalse();
}
