package com.szusta.meduva.controller;

import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.EmailResetService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/email")
public class EmailResetController {

    private EmailResetService emailResetService;
    private UserService userService;

    @Autowired
    private EmailResetController(
            EmailResetService emailResetService,
            UserService userService
    ){
        this.emailResetService = emailResetService;
        this.userService = userService;
    }

    @PostMapping("/request/{id}")
    public ResponseEntity<MessageResponse> sendEmailResetLink(@PathVariable final Integer id, @RequestBody final String email){
        if(userService.existsByEmail(email)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Email is already in use"));
        }else{
            User user = this.userService.findById(id);
            emailResetService.deletePreviousResetTokens(user);
            EmailResetToken emailResetToken = this.emailResetService.createEmailResetToken(user, email);
            emailResetService.sendEmailResetMail(email, emailResetToken);
            return ResponseEntity.ok(new MessageResponse("Email reset link has been sent to your email"));
        }

    }

    @PostMapping("/validate-email-reset-token")
    public ResponseEntity<MessageResponse> validateEmailResetToken(@RequestBody final String resetToken){

        EmailResetToken storedEmailResetToken = emailResetService.getEmailResetToken(resetToken);
        if(storedEmailResetToken.getExpiryDate().before(new Date())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Email reset token has expired"));
        } else{
            this.changePassword(storedEmailResetToken);
            return ResponseEntity.ok(new MessageResponse("Email changed successfully."));
        }
    }

    private void changePassword(EmailResetToken storedEmailResetToken){
        User user = storedEmailResetToken.getUser();
        String newEmail = storedEmailResetToken.getEmail();
        user.setEmail(newEmail);
        this.emailResetService.deletePreviousResetTokens(user);

        this.userService.save(user);
    }
}
