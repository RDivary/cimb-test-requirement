package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserDetailsImplTest {

    private UserDetailsImpl userDetailsImpl;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userId(1L)
                .username("username")
                .passwordHash("passwordHash")
                .role(RolesEnum.ROLE_USER)
                .build();
        userDetailsImpl = UserDetailsImpl.build(user);
    }

    @Test
    void testGetAuthorities() {
        assertEquals(1, userDetailsImpl.getAuthorities().size());
    }

    @Test
    void testGetPassword() {
        assertEquals("passwordHash", userDetailsImpl.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("username", userDetailsImpl.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetailsImpl.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetailsImpl.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetailsImpl.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetailsImpl.isEnabled());
    }
}