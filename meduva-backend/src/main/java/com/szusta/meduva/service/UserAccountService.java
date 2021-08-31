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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Value("${meduva.app.front_base_url}")
    String baseURL;

    @Autowired
    public UserAccountService(
            UserService userService,
            JwtUtils jwtUtils,
            PasswordResetTokenRepository passwordResetTokenRepository,
            JavaMailSender javaMailSender,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordResetToken createPasswordResetToken(User user) {

        String token = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
        PasswordResetToken resetToken = new PasswordResetToken(
                user,
                token,
                new Date(new Date().getTime() + jwtUtils.getJwtExpirationMs()));
        return passwordResetTokenRepository.save(resetToken);
    }

    public void deletePreviousResetTokens(User user) {
        passwordResetTokenRepository.deleteByUser(user);
    }

    public void sendResetPasswordEmail(String email, PasswordResetToken resetToken) {

        SimpleMailMessage mailMessage = createPasswordResetMailMessage(email, resetToken);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createPasswordResetMailMessage(String email, PasswordResetToken resetToken) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password reset link");
        msg.setText("Follow this link to reset your password:\n" + getResetLink(resetToken.getToken()));
        return msg;
    }

    private String getResetLink(String token) {
        return baseURL + "/login/password-reset/" + token;
    }

    public User getUserFromResetToken(String resetToken) {

        PasswordResetToken storedResetToken = passwordResetTokenRepository.findByToken(resetToken)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException("Password reset token not found"));

        return storedResetToken.getUser();
    }

    public PasswordResetToken getResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException("Password reset token not found"));
    }

    public void resetPassword(String resetToken, String newPassword) {
        User user = getUserFromResetToken(resetToken);
        user.setPassword(passwordEncoder.encode(newPassword));
        user = userService.save(user);
        deletePreviousResetTokens(user);
    }
}
