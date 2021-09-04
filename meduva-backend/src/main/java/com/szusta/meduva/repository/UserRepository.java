package com.szusta.meduva.repository;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);

    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);

    Optional<List<User>> findByRolesIn(Collection<Role> roles);
}
