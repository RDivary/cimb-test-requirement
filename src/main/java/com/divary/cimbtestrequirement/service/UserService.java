package com.divary.cimbtestrequirement.service;

import com.divary.cimbtestrequirement.model.User;

import java.util.Optional;

public interface UserService {
    User save(User user);

    Optional<User> findByUsername(String username);

    boolean isUsernameExist(String username);

    boolean checkDuplicateAccountNumber(String accountNumber);
}
