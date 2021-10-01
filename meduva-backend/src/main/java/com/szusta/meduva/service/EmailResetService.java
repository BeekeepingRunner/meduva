package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.EmailResetTokenRepository;
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
public class EmailResetService {

    private EmailResetTokenRepository emailResetTokenRepository;
    private JwtUtils jwtUtils;
    private JavaMailSender javaMailSender;

    @Value("${meduva.app.front_base_url}")
    String baseURL;

    @Autowired
    EmailResetService(
            EmailResetTokenRepository emailResetTokenRepository,
            JwtUtils jwtUtils,
            JavaMailSender javaMailSender
    ){
        this.emailResetTokenRepository = emailResetTokenRepository;
        this.jwtUtils = jwtUtils;
        this.javaMailSender = javaMailSender;
    }

    public void deletePreviousResetTokens(User user) {
        this.emailResetTokenRepository.deleteByUser(user);
    }

    public EmailResetToken createEmailResetToken(User user, String newEmail){
        String token = jwtUtils.generateJwtTokenFrom(UserDetailsImpl.build(user));
        EmailResetToken resetToken = new EmailResetToken(
                user,
                token,
                new Date(new Date().getTime() + jwtUtils.getJwtExpirationMs()),
                newEmail);
        return emailResetTokenRepository.save(resetToken);
    }

    public void sendEmailResetMail(String email, EmailResetToken emailResetToken) {
        SimpleMailMessage mailMessage = createEmailResetMailMessage(email, emailResetToken);
        this.javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createEmailResetMailMessage(String email, EmailResetToken emailResetToken) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Email reset link");
        msg.setText("Follow this link to activate your new email:\n" + getResetLink(emailResetToken.getToken()));
        return msg;
    }

    private String getResetLink(String token) {
        return baseURL + "/new-email-activation/" + token;
    }

    public EmailResetToken getEmailResetToken(String token){
        return emailResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityRecordNotFoundException("Email reset token not found : " + token));
    }
}
