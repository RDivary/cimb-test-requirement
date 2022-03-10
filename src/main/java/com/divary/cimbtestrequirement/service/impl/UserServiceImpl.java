package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.repository.UserRepository;
import com.divary.cimbtestrequirement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
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
}
