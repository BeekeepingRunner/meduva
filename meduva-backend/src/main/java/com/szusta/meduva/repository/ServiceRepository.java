package com.szusta.meduva.repository;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends UndeletableWithNameRepository<Service> {

}
