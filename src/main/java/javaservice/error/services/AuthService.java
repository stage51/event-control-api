package javaservice.error.services;

import javaservice.error.dtos.jwts.JwtRequest;
import javaservice.error.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest);
    ResponseEntity<?> createNewUser(@RequestBody User user);
}