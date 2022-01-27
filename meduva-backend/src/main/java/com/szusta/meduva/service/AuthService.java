package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.BadRequestRoleException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.RefreshToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.payload.response.JwtResponse;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.user.UserDetailsImpl;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private UserService userService;
    private UserRepository userRepository;

    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public AuthService(UserService userService,
                       UserRepository userRepository,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    public void checkForExistingCredentials(String login, String email) {

        if (userRepository.existsByLogin(login)) {
            throw new AlreadyExistsException("That login is already taken : " + login);
        }
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email is already in use : " + email);
        }
    }

    public void saveNewUserFrom(SignupRequest request) {
        Set<Role> roles = extractRequestRoles(request.getRoles());

        User user = new User(
                request.getLogin(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
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
                    throw new BadRequestRoleException("Bad user role in request body");
            }
        });
    }

    public JwtResponse authenticate(String login, String password) {

        Authentication authentication = authenticateUser(login, password);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtTokenFrom(userDetails);
        Set<Role> roles = userService.getUser(userDetails.getId()).getRoles();

        RefreshToken refreshToken = getRefreshTokenFrom(userDetails);
        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    private Authentication authenticateUser(String login, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private RefreshToken getRefreshTokenFrom(UserDetailsImpl userDetails) {
        refreshTokenService.deleteByUserId(userDetails.getId());
        return refreshTokenService.createRefreshToken(userDetails.getId());
    }

    public boolean validateUserJwt(String jwt) {
        return jwtUtils.hasJwtExpired(jwt);
    }

    public boolean checkIfUserIsNotDeleted(String login){
        User tempUser = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityRecordNotFoundException("User not found with login : " + login));
        return !tempUser.isDeleted();
    }
}
