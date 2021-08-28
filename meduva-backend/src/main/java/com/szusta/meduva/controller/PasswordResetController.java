package com.szusta.meduva.controller;

import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.ResetPasswordRequest;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.UserAccountService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    private UserAccountService userAccountService;
    private UserService userService;

    @Autowired
    public PasswordResetController(
            UserAccountService userAccountService,
            UserService userService) {
        this.userAccountService = userAccountService;
        this.userService = userService;
    }

    // Triggered when user sends email for the reset link
    @PostMapping("/request")
    public ResponseEntity<MessageResponse> sendResetPasswordEmail(@RequestBody final String email) {

        User user = userService.findByEmail(email);
        PasswordResetToken resetToken = userAccountService.createPasswordResetToken(user);
        userAccountService.deletePreviousResetTokens(user);
        userAccountService.sendResetPasswordEmail(email, resetToken);
        return ResponseEntity.ok(new MessageResponse("Password reset link has been sent to your email"));
    }

    // Triggered when user sends new password
    @PostMapping("/change")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody ResetPasswordRequest resetPasswordRequest)
    {
        // TODO: change password
        String resetToken = resetPasswordRequest.getResetToken();
        User user = userAccountService.getUserFromResetToken(resetToken);
        if (user != null) {
            return ResponseEntity.ok(new MessageResponse("Backend password change test successful!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Couldn't authenticate user"));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<User> getUserWithResetToken(@RequestBody String resetToken) {

        User user = userAccountService.getUserFromResetToken(resetToken);
        return ResponseEntity.ok(user);
    }
}