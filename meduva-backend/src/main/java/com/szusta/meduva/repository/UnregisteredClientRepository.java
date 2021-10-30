package com.szusta.meduva.repository;

import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnregisteredClientRepository extends UndeletableRepository<UnregisteredClient> {

}