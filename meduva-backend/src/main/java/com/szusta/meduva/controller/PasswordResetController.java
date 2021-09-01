package com.szusta.meduva.controller;

import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.password.ResetPasswordRequest;
import com.szusta.meduva.payload.request.password.ResetTokenRequest;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.service.PasswordResetService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    private PasswordResetService passwordResetService;
    private UserService userService;

    @Autowired
    public PasswordResetController(
            PasswordResetService passwordResetService,
            UserService userService) {
        this.passwordResetService = passwordResetService;
        this.userService = userService;
    }

    // Triggered when user sends email for the reset link
    @PostMapping("/request")
    public ResponseEntity<MessageResponse> sendResetPasswordEmail(@RequestBody final String email) {

        User user = userService.findByEmail(email);
        passwordResetService.deletePreviousResetTokens(user);
        PasswordResetToken resetToken = passwordResetService.createPasswordResetToken(user);
        passwordResetService.sendResetPasswordEmail(email, resetToken);
        return ResponseEntity.ok(new MessageResponse("Password reset link has been sent to your email"));
    }

    // Triggered after user clicks reset link
    @PostMapping("/validate-reset-token")
    public ResponseEntity<MessageResponse> validateResetToken(@RequestBody final ResetTokenRequest resetToken) {

        PasswordResetToken storedResetToken = passwordResetService.getResetToken(resetToken.getToken());
        if (storedResetToken.getExpiryDate().before(new Date())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Reset token has expired"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Request token is valid."));
        }
    }

    // Triggered when user sends new password
    @PostMapping("/change")
    public ResponseEntity<MessageResponse> handlePasswordChangeRequest(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest)
    {
        String resetToken = resetPasswordRequest.getResetToken();
        passwordResetService.resetPassword(resetToken, resetPasswordRequest.getPassword());

        return ResponseEntity.ok(new MessageResponse("Password has been changed!"));
    }

    @PostMapping("/user")
    public ResponseEntity<User> getUserWithResetToken(@RequestBody String resetToken) {

        User user = passwordResetService.getUserFromResetToken(resetToken);
        return ResponseEntity.ok(user);
    }
}