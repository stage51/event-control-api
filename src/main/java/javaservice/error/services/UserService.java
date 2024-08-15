package javaservice.error.services;

import javaservice.error.models.User;

import java.security.Principal;
import java.util.Optional;

public interface UserService extends CrudService<User, User>{
    Optional<User> findByUsername(String username);
    User getUserByPrincipal(Principal principal);
}
