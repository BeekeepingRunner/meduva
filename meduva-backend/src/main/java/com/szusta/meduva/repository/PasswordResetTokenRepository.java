package com.szusta.meduva.repository;

import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Override
    Optional<PasswordResetToken> findById(Long id);
    Optional<PasswordResetToken> findByToken(String token);

    int deleteByUser(User user);
}
