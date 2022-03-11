package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.ForbiddenException;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserDetailsServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }

    @Test
    void testLoadUserByUsername_OK() {
        User user = User.builder()
                .role(RolesEnum.ROLE_USER)
                .build();

        when(userService.findByUsername("username")).thenReturn(Optional.of(user));

        assertNotNull(userDetailsServiceImpl.loadUserByUsername("username"));
    }

    @Test
    void testLoadUserByUsername_Failed_whenUserNotFound() {
        when(userService.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> userDetailsServiceImpl.loadUserByUsername("username"));
    }
}