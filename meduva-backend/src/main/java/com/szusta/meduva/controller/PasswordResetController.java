package com.szusta.meduva.controller;

import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    private UserAccountService userAccountService;

    @Autowired
    public PasswordResetController(
            UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Triggered when user sends email for the reset link
    @PostMapping("/request")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody final String email) {
        
        // TODO: make sure that emailNotFound exception is handled here
        userAccountService.sendResetPasswordEmail(email);
        return ResponseEntity.ok(new MessageResponse("Password reset link has been sent to your email"));
    }

    // Triggered when user clicks reset link
    @GetMapping("/change")
    public String changePassword(
            @RequestParam(required = false) String token,
            final RedirectAttributes redirectAttributes,
            final Model model)
    {
        return "";
    }

    /*
    // Triggered when user sends new password
    @PostMapping("/change")
    public String changePassword(final PasswordResetRequest requestData) {

    }

     */

    @GetMapping("/mail-test")
    public ResponseEntity<MessageResponse> mailTest() {

        userAccountService.sendTestMail("biegbart.z@gmail.com");
        return ResponseEntity.ok(new MessageResponse("Password reset link has been sent to your email"));
    }
}