package com.szusta.meduva.repository;

import com.szusta.meduva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
// @CrossOrigin(origins = "http://localhost:4200")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}
