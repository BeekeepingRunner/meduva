package com.szusta.meduva.repository;

import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
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
    //Optional<User> findById(long id);

    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Optional<List<User>> findDistinctByRolesIn(Collection<Role> roles);

    @Query("select distinct u, count(role) from User u join u.roles role group by u having count(role) = 1")
    Optional<List<User>> findAllClientsWithAccount();
}
