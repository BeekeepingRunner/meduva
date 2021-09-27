package com.szusta.meduva.controller;

import com.szusta.meduva.model.EmailResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.EmailResetService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/request")
    public ResponseEntity<MessageResponse> sendEmailResetLink(@RequestBody final int id, @RequestBody final String email){
        

    }
}
