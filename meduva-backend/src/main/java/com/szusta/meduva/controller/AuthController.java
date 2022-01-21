package com.szusta.meduva.controller;

import com.szusta.meduva.exception.TokenRefreshException;
import com.szusta.meduva.model.RefreshToken;
import com.szusta.meduva.payload.request.LoginRequest;
import com.szusta.meduva.payload.request.RefreshTokenRequest;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.payload.response.JwtResponse;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.payload.response.RefreshTokenResponse;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.AuthService;
import com.szusta.meduva.service.RefreshTokenService;
import com.szusta.meduva.service.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;
    JwtUtils jwtUtils;
    RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        String login = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        if(authService.checkIfUserIsNotDeleted(login)){
            JwtResponse jwtResponse = authService.authenticate(login, password);
            return ResponseEntity.ok(jwtResponse);
        } else {
            throw new RuntimeException("This user has been deleted!");
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody SignupRequest signupRequest) {

        String login = signupRequest.getLogin();
        String email = signupRequest.getEmail();
        authService.checkForExistingCredentials(login, email);

        authService.saveNewUserFrom(signupRequest);
        return ResponseEntity.ok(
                new MessageResponse("User registered successfully"));
    }

    @PostMapping("/validate-jwt")
    public boolean validateUserJwt(@RequestBody String jwt) {
        return authService.validateUserJwt(jwt);
    }

    @PostMapping("/refreshtoken")
    // request should be @Valid
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        String requestRefreshToken = request.getRefreshToken();

        String newAccessToken = refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> jwtUtils.generateJwtTokenFrom(UserDetailsImpl.build(user)))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

        return ResponseEntity.ok().body(
                new RefreshTokenResponse(newAccessToken, requestRefreshToken));
    }
}
