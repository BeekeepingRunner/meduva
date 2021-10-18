package com.szusta.meduva.controller;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.RefreshToken;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.payload.request.LoginRequest;
import com.szusta.meduva.payload.request.RefreshTokenRequest;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.payload.response.JwtResponse;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.AuthService;
import com.szusta.meduva.service.RefreshTokenService;
import com.szusta.meduva.service.RoleService;
import com.szusta.meduva.service.user.UserDetailsImpl;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    AuthenticationManager authenticationManager;
    UserService userService;
    RoleService roleService;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          UserService userService,
                          RoleService roleService,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticateUser(loginRequest);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtTokenFrom(userDetails);
        Set<Role> roles = userService.getUser(userDetails.getId()).getRoles();

        // Not fully implemented yet !!!
        RefreshToken refreshToken = getRefreshTokenFrom(userDetails);

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        refreshToken.getToken(),
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private RefreshToken getRefreshTokenFrom(UserDetailsImpl userDetails) {
        refreshTokenService.deleteByUserId(userDetails.getId());    // delete previous refresh tokens
        return refreshTokenService.createRefreshToken(userDetails.getId());
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
        return jwtUtils.hasJwtExpired(jwt);
    }

    @PostMapping("/refreshtoken")
    // request should be @Valid
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        String requestRefreshToken = request.getRefreshToken();

        String outRefreshToken =  refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtTokenFrom(UserDetailsImpl.build(user));
                    return token;
                })
                .orElseThrow(() -> new EntityRecordNotFoundException("Refresh token is not in database!"));

        return ResponseEntity.ok().body(outRefreshToken);
    }
}
