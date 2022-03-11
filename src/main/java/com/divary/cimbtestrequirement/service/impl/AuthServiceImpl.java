package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.BadRequestException;
import com.divary.cimbtestrequirement.dto.request.auth.AuthReq;
import com.divary.cimbtestrequirement.dto.response.auth.LoginResp;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import com.divary.cimbtestrequirement.service.AuthService;
import com.divary.cimbtestrequirement.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void register(AuthReq form, RolesEnum role) throws NoSuchAlgorithmException {
        if (userService.isUsernameExist(form.getUsername()))
            throw new BadRequestException("This username isn't available. Please try another.");

        User user = User.builder()
                .username(form.getUsername())
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .accountNumber(generatedAccountNumber())
                .role(role)
                .build();

        userService.save(user);
        log.info("Username '{}' has bean created", user.getUsername());
    }

    @Override
    public LoginResp login(AuthReq form) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword())
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return LoginResp.builder()
                .username(form.getUsername())
                .token(jwtUtils.generateJwtToken(userDetails))
                .build();
    }

    private String generatedAccountNumber() throws NoSuchAlgorithmException {
        Random random;
        String accountNumber;
        do {
            StringBuilder builderAccountNumber = new StringBuilder();
            random = SecureRandom.getInstanceStrong();

            for (int i = 1; i < 8; i++) {
                builderAccountNumber.append(random.nextInt(10));
            }
            accountNumber = builderAccountNumber.toString();
        } while (userService.checkDuplicateAccountNumber(accountNumber));
        return accountNumber;
    }
}
