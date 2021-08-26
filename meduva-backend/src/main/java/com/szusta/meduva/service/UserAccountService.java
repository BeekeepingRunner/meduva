package com.szusta.meduva.service;

import com.szusta.meduva.exception.PasswordResetTokenNotFoundException;
import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.PasswordResetTokenRepository;
import com.szusta.meduva.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserAccountService {

    private UserService userService;
    private JwtUtils jwtUtils;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private JavaMailSender javaMailSender;

    @Value("${meduva.app.front_base_url}")
    String baseURL;

    @Autowired
    public UserAccountService(
            UserService userService,
            JwtUtils jwtUtils,
            PasswordResetTokenRepository passwordResetTokenRepository,
            JavaMailSender javaMailSender) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    public void sendResetPasswordEmail(String email) {

        User user = userService.findByEmail(email);
        String token = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
        PasswordResetToken resetToken = new PasswordResetToken(
                user,
                token,
                new Date(new Date().getTime() + jwtUtils.getJwtExpirationMs()));
        deletePreviousResetTokens(user);
        passwordResetTokenRepository.save(resetToken);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password reset link");
        msg.setText("Follow this link to reset your password:\n" + getResetLink(token));

        javaMailSender.send(msg);
    }

    private void deletePreviousResetTokens(User user) {
        passwordResetTokenRepository.deleteByUser(user);
    }

    private String getResetLink(String token) {
        return baseURL + "/login/password-reset/" + token;
    }

    public User getUserFromResetToken(String resetToken) {

        PasswordResetToken storedResetToken = passwordResetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException("Password reset token not found"));

        return storedResetToken.getUser();
    }
}
