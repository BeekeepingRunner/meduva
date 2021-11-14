package com.szusta.meduva.repository;

import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnregisteredClientRepository extends UndeletableRepository<UnregisteredClient> {

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT uc.id, "
                    + "uc.name, uc.surname, uc.phone_number, uc.deleted FROM unregistered_client uc "
                    + "INNER JOIN visit vv ON vv.unregistered_client_id = uc.id "
                    + "WHERE vv.id IN "
                    + "(SELECT v.id FROM visit v "
                    + "INNER JOIN user_visit uv ON v.id = uv.visit_id "
                    + "WHERE uv.user_id = ?1 AND uv.as_client = 0)"
    )
    List<UnregisteredClient> findAllUnregisteredClientsOfWorker(long workerId);
}
