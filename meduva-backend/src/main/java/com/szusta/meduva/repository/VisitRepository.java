package com.szusta.meduva.repository;

import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends UndeletableRepository<User> {

}
