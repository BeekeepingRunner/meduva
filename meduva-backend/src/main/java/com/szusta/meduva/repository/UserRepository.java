package com.szusta.meduva.repository;

import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends UndeletableRepository<User> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);

    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Optional<List<User>> findDistinctByRolesIn(Collection<Role> roles);

    @Query("select distinct u, count(role) from User u join u.roles role group by u having count(role) = 1")
    Optional<List<User>> findAllClientsWithAccount();

    @Query("select distinct u from User u join u.services s where s.id = ?1")
    List<User> findAllByPerformedService(long serviceId);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT u.id, u.email, u.login, u.password, "
            + "u.name, u.surname, u.phone_number, u.deleted FROM user u "
            + "INNER JOIN user_visit userv ON userv.user_id = u.id "
            + "WHERE userv.as_client = 1 AND userv.visit_id IN "
            + "(SELECT v.id FROM visit v "
            + "INNER JOIN user_visit uv ON v.id = uv.visit_id "
            + "WHERE uv.user_id = ?1 AND uv.as_client = 0)"
    )
    List<User> findAllClientsOfWorker(long workerId);
}
