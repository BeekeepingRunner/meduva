package com.szusta.meduva.controller;

import com.szusta.meduva.exception.BadRequestRole;
import com.szusta.meduva.exception.EmailAlreadyInUseException;
import com.szusta.meduva.exception.LoginAlreadyTakenException;
import com.szusta.meduva.exception.TokenRefreshException;
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

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        checkForExistingCredentials(signupRequest);
        saveNewUserFrom(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    private void checkForExistingCredentials(SignupRequest request) {

        if (userService.existsByLogin(request.getLogin())) {
            throw new LoginAlreadyTakenException("Error: That login is already taken");
        }
        if (userService.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("Error: That email is already in use");
        }
    }

    private void saveNewUserFrom(SignupRequest request) {

        Set<Role> roles = extractRequestRoles(request.getRoles());

        User user = new User(
                request.getLogin(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname(),
                request.getPhoneNumber());

        user.setRoles(roles);
        userService.save(user);
    }

    private Set<Role> extractRequestRoles(Set<String> requestRoles) {

        Set<Role> userRoles = new HashSet<>();

        if (requestRoles == null) {
            addDefaultRoleTo(userRoles);
        } else {
            addRoles(requestRoles, userRoles);
        }

        return userRoles;
    }

    private void addDefaultRoleTo(Set<Role> userRoles) {
        userRoles.add(roleService.findByName("ROLE_CLIENT"));
    }

    private void addRoles(Set<String> requestRoles, Set<Role> userRoles) {

        requestRoles.forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN":
                    userRoles.add(roleService.findByName("ROLE_ADMIN"));
                    break;
                case "ROLE_RECEPTIONIST":
                    userRoles.add(roleService.findByName("ROLE_RECEPTIONIST"));
                    break;
                case "ROLE_WORKER":
                    userRoles.add(roleService.findByName("ROLE_WORKER"));
                    break;
                case "ROLE_CLIENT":
                    userRoles.add(roleService.findByName("ROLE_CLIENT"));
                    break;
                default:
                    throw new BadRequestRole("Bad user role in request body");
            }
        });
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
                .orElseThrow(() -> new TokenRefreshException(
                        requestRefreshToken, "Refresh token is not in database!"));

        return ResponseEntity.ok().body(outRefreshToken);
    }
}
