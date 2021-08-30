package com.szusta.meduva.controller;

import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.ResetPasswordRequest;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.UserAccountService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    private UserAccountService userAccountService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetController(
            UserAccountService userAccountService,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.userAccountService = userAccountService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Triggered when user sends email for the reset link
    @PostMapping("/request")
    public ResponseEntity<MessageResponse> sendResetPasswordEmail(@RequestBody final String email) {

        User user = userService.findByEmail(email);
        userAccountService.deletePreviousResetTokens(user);
        PasswordResetToken resetToken = userAccountService.createPasswordResetToken(user);
        userAccountService.sendResetPasswordEmail(email, resetToken);
        return ResponseEntity.ok(new MessageResponse("Password reset link has been sent to your email"));
    }

    // Triggered when user sends new password
    // TODO: validate reset request
    @PostMapping("/change")
    public ResponseEntity<MessageResponse> handlePasswordChangeRequest(@RequestBody ResetPasswordRequest resetPasswordRequest)
    {
        String resetToken = resetPasswordRequest.getResetToken();
        User user = userAccountService.getUserFromResetToken(resetToken);
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userService.save(user);
        userAccountService.deletePreviousResetTokens(user);
        return ResponseEntity.ok(new MessageResponse("Password has been changed!"));
    }

    @PostMapping("/user")
    public ResponseEntity<User> getUserWithResetToken(@RequestBody String resetToken) {

        User user = userAccountService.getUserFromResetToken(resetToken);
        return ResponseEntity.ok(user);
    }
}