package com.szusta.meduva.repository.schedule.visit;

import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends UndeletableRepository<Visit> {

}
