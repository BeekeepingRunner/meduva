package com.szusta.meduva.controller;

import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.LoginRequest;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.payload.response.JwtResponse;
import com.szusta.meduva.payload.response.MessageResponse;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.security.jwt.JwtUtils;
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

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          RoleService roleService,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    // Login request should be @Valid
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    // SignupRequest should be @Valid
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        if (userService.existsByLogin(signupRequest.getLogin())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        Set<Role> roles;
        try {
            roles = processRequestRoles(signupRequest.getRole());
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

        User user = new User(
                signupRequest.getLogin(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        user.setRoles(roles);
        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    private Set<Role> processRequestRoles(Set<String> requestStringRoles) {

        Set<Role> userRoles = new HashSet<>();

        if (requestStringRoles == null) {
            Role userRole = roleService.findByName(ERole.ROLE_CLIENT);
            userRoles.add(userRole);
        } else {
            requestStringRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findByName(ERole.ROLE_ADMIN);
                        userRoles.add(adminRole);
                        break;
                    case "receptionist":
                        Role receptionistRole = roleService.findByName(ERole.ROLE_RECEPTIONIST);
                        userRoles.add(receptionistRole);
                        break;
                    case "worker":
                        Role workerRole = roleService.findByName(ERole.ROLE_WORKER);
                        userRoles.add(workerRole);
                        break;
                    case "client":
                        Role clientRole = roleService.findByName(ERole.ROLE_CLIENT);
                        userRoles.add(clientRole);
                        break;
                    default:
                        throw new RuntimeException("Bad user role in request body");
                }
            });
        }
        
        return userRoles;
    }
}
