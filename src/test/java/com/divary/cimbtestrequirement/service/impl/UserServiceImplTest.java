package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }

    @Test
    void testSave_OK() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        assertNotNull(userServiceImpl.save(new User()));
    }

    @Test
    void testFindByUsername_OK() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));

        assertTrue(userServiceImpl.findByUsername("username").isPresent());
    }

    @Test
    void testFindByUsername_WhenUserInNotFound() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertFalse(userServiceImpl.findByUsername("username").isPresent());
    }

    @Test
    void testIsUsernameExist_OK_WhenReturnisTrue() {
        when(userRepository.existsByUsername("username")).thenReturn(true);

        assertTrue(userServiceImpl.isUsernameExist("username"));
    }

    @Test
    void testIsUsernameExist_OK_whenReturnIsFalse() {
        when(userRepository.existsByUsername("username")).thenReturn(false);

        assertFalse(userServiceImpl.isUsernameExist("username"));
    }

    @Test
    void testCheckDuplicateAccountNumber_OK_WhenReturnisTrue() {
        when(userRepository.existsByAccountNumber("12345678")).thenReturn(true);

        assertTrue(userServiceImpl.checkDuplicateAccountNumber("12345678"));
    }

    @Test
    void testcheckDuplicateAccountNumber_OK_whenReturnIsFalse() {
        when(userRepository.existsByAccountNumber("12345678")).thenReturn(false);

        assertFalse(userServiceImpl.checkDuplicateAccountNumber("12345678"));
    }
}