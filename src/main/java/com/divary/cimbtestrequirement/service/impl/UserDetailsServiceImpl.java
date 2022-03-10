package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.ForbiddenException;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) {

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ForbiddenException("User Not Found"));

        return UserDetailsImpl.build(user);
    }

}
