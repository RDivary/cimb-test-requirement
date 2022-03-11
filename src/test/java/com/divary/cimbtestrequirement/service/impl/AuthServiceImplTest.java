package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.BadRequestException;
import com.divary.cimbtestrequirement.dto.request.auth.AuthReq;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import com.divary.cimbtestrequirement.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private AuthReq authReq;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }


    @BeforeEach
    void setUp() {
        authReq = AuthReq.builder()
                .username("username")
                .password("password")
                .build();
    }

    @Test
    void testRegister_OK() throws NoSuchAlgorithmException {
        when(userService.isUsernameExist(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashPassword");
        when(userService.save(any(User.class))).thenReturn(new User());

        authServiceImpl.register(authReq, RolesEnum.ROLE_USER);

        verify(userService).save(any(User.class));
    }

    @Test
    void testRegister_Failed_whenUsernameNotFound() {
        when(userService.isUsernameExist(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authServiceImpl.register(authReq, RolesEnum.ROLE_USER));

        verify(userService, times(0)).save(any(User.class));
    }
}