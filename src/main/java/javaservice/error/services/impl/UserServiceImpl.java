package javaservice.error.services.impl;

import javaservice.error.exceptions.EntityNotFoundException;
import javaservice.error.models.User;
import javaservice.error.models.User;
import javaservice.error.repos.UserRepository;
import javaservice.error.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User entity) {
        /*
        if (read(entity.getId()).isPresent()) {
            return false;
        }
        */
        log.info("User created");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<User> read(Long id) {
        Optional<User> entity = Optional.of(userRepository.findById(id)).orElse(Optional.empty());
        if (entity.isEmpty()){
            log.error("Failed to read user with id " + id);
            throw new EntityNotFoundException("User", id);
        }
        log.info("User read with id " + id);
        return entity;
    }

    @Override
    public User update(User entity) {
        if (read(entity.getId()).isEmpty()) {
            log.error("Failed to update user with id " + entity.getId());
            throw new EntityNotFoundException("User", entity.getId());
        }
        log.info("User updated with id " + entity.getId());
        return userRepository.saveAndFlush(entity);
    }

    @Override
    public boolean delete(Long id) {
        Optional<User> entity = read(id);
        if (read(id).isEmpty()) {
            log.error("Failed to delete user with id " + id);
            throw new EntityNotFoundException("User", id);
        }
        userRepository.delete(entity.get());
        log.info("User deleted with id " + id);
        return true;
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> readPage(int number) {
        return userRepository.findAll(PageRequest.of(number, PAGE_SIZE));
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        Optional<User> user = findByUsername(principal.getName());
        return user.get();
    }
}
