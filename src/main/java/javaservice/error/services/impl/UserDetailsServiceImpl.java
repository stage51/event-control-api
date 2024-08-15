package javaservice.error.services.impl;

import javaservice.error.exceptions.IncorrectLoginOrPasswordException;
import javaservice.error.models.User;
import javaservice.error.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j

public class UserDetailsServiceImpl implements UserDetailsService {
    private UserService userService;
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws IncorrectLoginOrPasswordException {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            log.error("User not found with username " + username);
            throw new IncorrectLoginOrPasswordException("Incorrect login or password");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(), user.get().getPassword(), new ArrayList<>()
        );
    }
}
