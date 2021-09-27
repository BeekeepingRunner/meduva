package com.szusta.meduva.repository;

import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailResetTokenRepository extends JpaRepository<EmailResetToken, Long> {

    int deleteByUser(User user);
}
