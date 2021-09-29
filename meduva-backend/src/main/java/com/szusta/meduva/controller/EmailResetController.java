package com.szusta.meduva.controller;

import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.EmailResetService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        User user = this.userService.findById(id);
        emailResetService.deletePreviousResetTokens(user);
        EmailResetToken emailResetToken = this.emailResetService.createEmailResetToken(user, email);
        emailResetService.sendEmailResetMail(email, emailResetToken);

        return ResponseEntity.ok(new MessageResponse("Email reset link has been sent to your email"));
    }
}
