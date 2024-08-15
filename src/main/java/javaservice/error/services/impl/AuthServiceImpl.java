package javaservice.error.services.impl;

import javaservice.error.dtos.jwts.JwtRequest;
import javaservice.error.dtos.jwts.JwtResponse;
import javaservice.error.exceptions.IncorrectLoginOrPasswordException;
import javaservice.error.models.User;
import javaservice.error.services.AuthService;
import javaservice.error.services.UserService;
import javaservice.error.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private UserDetailsServiceImpl userDetailsService;
    private UserService userService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;
    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setJwtTokenUtils(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e) {
            log.error("Incorrect login or password");
            throw new IncorrectLoginOrPasswordException("Incorrect login or password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        log.info("Created token " + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            log.error("User with username " + user.getUsername() + " is exist");
            throw new IncorrectLoginOrPasswordException("User with this email is exist");
        }
        userService.create(user);
        log.info("Registered user with id " + user.getId());
        return ResponseEntity.ok(user);
    }
}