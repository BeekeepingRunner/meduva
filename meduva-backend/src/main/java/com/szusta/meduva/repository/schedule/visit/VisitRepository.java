package com.szusta.meduva.repository.schedule.visit;

import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends UndeletableRepository<Visit> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM visit v "
                    + "INNER JOIN user_visit uv ON visit_id = v.id "
                    + "WHERE as_client = 1 AND uv.user_id = ?1 "
                    + "ORDER BY v.time_from DESC "
    )
    List<Visit> findAllWhereUserIsClient(Long userClientId);
}
