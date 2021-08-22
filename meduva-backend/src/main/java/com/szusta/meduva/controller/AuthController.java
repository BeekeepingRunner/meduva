package com.szusta.meduva.controller;

import com.szusta.meduva.exception.EmailAlreadyInUseException;
import com.szusta.meduva.exception.LoginAlreadyTakenException;
import com.szusta.meduva.exception.RoleNotFoundException;
import com.szusta.meduva.exception.TokenRefreshException;
import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.RefreshToken;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.LoginRequest;
import com.szusta.meduva.payload.request.RefreshTokenRequest;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.payload.response.JwtResponse;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.RefreshTokenService;
import com.szusta.meduva.service.RoleService;
import com.szusta.meduva.service.UserDetailsImpl;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserService userService;
    RoleService roleService;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          RoleService roleService,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    // Login request should be @Valid
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        refreshTokenService.deleteByUserId(userDetails.getId());    // delete previous refresh tokens
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/refreshtoken")
    // request should be @Valid
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        String requestRefreshToken = request.getRefreshToken();

        String outRefreshToken =  refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
                    return token;
                })
                .orElseThrow(() -> new TokenRefreshException(
                        requestRefreshToken, "Refresh token is not in database!"));

        return ResponseEntity.ok().body(outRefreshToken);
    }

    @PostMapping("/signup")
    // SignupRequest should be @Valid
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        if (userService.existsByLogin(signupRequest.getLogin())) {
            throw new LoginAlreadyTakenException("Error: That login is already taken");
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Error: That email is already in use");
        }

        Set<Role> roles = processRequestRoles(signupRequest.getRole());

        User user = new User(
                signupRequest.getLogin(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getName(),
                signupRequest.getSurname(),
                signupRequest.getPhoneNumber());

        user.setRoles(roles);
        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    private Set<Role> processRequestRoles(Set<String> requestRoles) {

        Set<Role> userRoles = new HashSet<>();

        if (requestRoles == null) {
            Role userRole = roleService.findByName("ROLE_CLIENT");
            userRoles.add(userRole);
        } else {
            requestRoles.forEach(role -> {

                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleService.findByName("ROLE_ADMIN");
                        userRoles.add(adminRole);
                        break;
                    case "ROLE_RECEPTIONIST":
                        Role receptionistRole = roleService.findByName("ROLE_RECEPTIONIST");
                        userRoles.add(receptionistRole);
                        break;
                    case "ROLE_WORKER":
                        Role workerRole = roleService.findByName("ROLE_WORKER");
                        userRoles.add(workerRole);
                        break;
                    case "ROLE_CLIENT":
                        Role clientRole = roleService.findByName("ROLE_CLIENT");
                        userRoles.add(clientRole);
                        break;
                    default:
                        throw new RoleNotFoundException("Bad user role in request body");
                }
            });
        }

        return userRoles;
    }
}
