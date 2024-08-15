package javaservice.error.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javaservice.error.dtos.jwts.JwtResponse;
import javaservice.error.models.User;
import javaservice.error.services.AuthService;
import javaservice.error.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javaservice.error.dtos.jwts.JwtRequest;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "Authentication", description = "Authorization is required to work with the rest of the application")
public class AuthResource {
    private AuthService authService;
    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    @Operation(summary = "The user enters an email and password, and after successful login receives a JWT token", tags = "Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        log.info("Auth request");
        return authService.createAuthToken(authRequest);
    }
    @Operation(summary = "The user enters the nickname, email, password and password again, and after successful entry creates an account", tags = "Registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        log.info("Register request");
        return authService.createNewUser(user);
    }
    @Operation(summary = "Exiting the session", tags = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Logout request");
        return ResponseEntity.ok().build();
    }
}