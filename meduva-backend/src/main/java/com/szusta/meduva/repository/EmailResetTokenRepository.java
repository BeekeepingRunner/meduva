package com.szusta.meduva.repository;

import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailResetTokenRepository extends JpaRepository<EmailResetToken, Long> {

    Optional<EmailResetToken> findByToken(String token);

    int deleteByUser(User user);
}
