package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.NotFoundException;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.repository.UserRepository;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import com.divary.cimbtestrequirement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String NOT_FOUND = "user not found";
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUser(String jwt, Long id) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        String role = jwtUtils.getRoleFromJwtToken(jwt);

        if (RolesEnum.ROLE_ADMIN.toString().equalsIgnoreCase(role)) {
            if (null == id) return findByUsername(username).orElseThrow(() -> new NotFoundException(NOT_FOUND));
            else return findById(id);
        } else if (RolesEnum.ROLE_USER.toString().equalsIgnoreCase(role)) {
            return findByUsername(username).orElseThrow(() -> new NotFoundException(NOT_FOUND));
        } else {
            throw new NotFoundException(NOT_FOUND);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkDuplicateAccountNumber(String accountNumber) {
        return userRepository.existsByAccountNumber(accountNumber);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }
}
