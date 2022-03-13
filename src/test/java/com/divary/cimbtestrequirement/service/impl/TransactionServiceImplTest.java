package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.ForbiddenException;
import com.divary.cimbtestrequirement.config.handler.exception.NotFoundException;
import com.divary.cimbtestrequirement.dto.request.transaction.ExecuteReq;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.TransactionHistory;
import com.divary.cimbtestrequirement.model.TransactionType;
import com.divary.cimbtestrequirement.model.User;
import com.divary.cimbtestrequirement.repository.TransactionHistoryRepository;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import com.divary.cimbtestrequirement.service.TransactionTypeService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TransactionServiceImplTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionTypeService transactionTypeService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    private User user;
    private TransactionType transactionType;
    private TransactionHistory transactionHistory;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .username("username")
                .accountNumber("12345678")
                .role(RolesEnum.ROLE_USER)
                .build();

        transactionType = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX001")
                .transactionName("Transfer")
                .active(true)
                .build();

        transactionHistory = TransactionHistory.builder()
                .transactionHistoryId(1L)
                .amount(10000L)
                .user(user)
                .transactionType(transactionType)
                .build();
    }

    @Test
    void testExecute_OK() {
        ExecuteReq executeReq = ExecuteReq.builder()
                .transactionTypeId(1L)
                .amount(10000L)
                .build();

        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(Optional.of(user));
        when(transactionTypeService.findById(1L)).thenReturn(transactionType);
        when(transactionHistoryRepository.save(any(TransactionHistory.class))).thenReturn(transactionHistory);

        TransactionHistory result = transactionServiceImpl.execute("jwt", executeReq);

        assertEquals(1L, result.getTransactionHistoryId());
        assertEquals(10000L, result.getAmount());
        assertNotNull(result.getUser());
        assertNotNull(result.getTransactionType());
    }

    @Test
    void testExecute_Failed_whenUserNotFound() {
        ExecuteReq executeReq = ExecuteReq.builder()
                .transactionTypeId(1L)
                .amount(10000L)
                .build();

        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionServiceImpl.execute("jwt", executeReq));
    }

    @Test
    void testExecute_FAILED_whenTransactionTypeNotActive() {
        ExecuteReq executeReq = ExecuteReq.builder()
                .transactionTypeId(1L)
                .amount(10000L)
                .build();
        transactionType.setActive(false);

        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(Optional.of(user));
        when(transactionTypeService.findById(1L)).thenReturn(transactionType);

        assertThrows(NotFoundException.class, () -> transactionServiceImpl.execute("jwt", executeReq));
    }

    @Test
    void testFindById_OK_whenRoleIsUser() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(transactionHistory));
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_USER.toString());

        TransactionHistory result = transactionServiceImpl.findById("jwt", 1L);

        assertEquals(1L, result.getTransactionHistoryId());
        assertEquals(10000L, result.getAmount());
        assertNotNull(result.getUser());
        assertNotNull(result.getTransactionType());
    }

    @Test
    void testFindById_OK_whenRoleIsAdmin() {
        user.setRole(RolesEnum.ROLE_ADMIN);
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(transactionHistory));
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_ADMIN.toString());

        TransactionHistory result = transactionServiceImpl.findById("jwt", 1L);

        assertEquals(1L, result.getTransactionHistoryId());
        assertEquals(10000L, result.getAmount());
        assertNotNull(result.getUser());
        assertNotNull(result.getTransactionType());
    }

    @Test
    void testFindById_FAILED_whenTransactionHistoryIsNull() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionServiceImpl.findById("jwt", 1L));
    }

    @Test
    void testFindById_FAILED_whenUsernameIsNotSame() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("user");
        when(transactionHistoryRepository.findById(1L)).thenReturn(Optional.of(transactionHistory));
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_USER.toString());

        assertThrows(ForbiddenException.class, () -> transactionServiceImpl.findById("jwt", 1L));
    }

    @Test
    void testFindAll_OK_whenRoleIsUserAndTransactionNameIsEmpty() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_USER.toString());
        when(transactionHistoryRepository.findAllByUser_username("username")).thenReturn(Arrays.asList(transactionHistory, transactionHistory));

        List<TransactionHistory> result = transactionServiceImpl.findAll("jwt", 1L, "");

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_OK_whenRoleIsUserAndTransactionName() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_USER.toString());
        when(transactionHistoryRepository.findAllByUser_usernameAndTransactionType_transactionCodeEquals("username", "trxName")).thenReturn(Arrays.asList(transactionHistory, transactionHistory));

        List<TransactionHistory> result = transactionServiceImpl.findAll("jwt", 1L, "trxName");

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_OK_whenRoleIsAdminAndTransactionNameIsEmpty() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_ADMIN.toString());
        when(transactionHistoryRepository.findAllByUser_userId(1L)).thenReturn(Arrays.asList(transactionHistory, transactionHistory));

        List<TransactionHistory> result = transactionServiceImpl.findAll("jwt", 1L, "");

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_OK_whenRoleIsAdmin() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn(RolesEnum.ROLE_ADMIN.toString());
        when(transactionHistoryRepository.findAllByUser_userIdAndTransactionType_transactionCodeEquals(1L, "trxName")).thenReturn(Arrays.asList(transactionHistory, transactionHistory));

        List<TransactionHistory> result = transactionServiceImpl.findAll("jwt", 1L, "trxName");

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll_OK_whenRoleIsEmpty() {
        when(jwtUtils.getUserNameFromJwtToken("jwt")).thenReturn("username");
        when(jwtUtils.getRoleFromJwtToken("jwt")).thenReturn("");

        List<TransactionHistory> result = transactionServiceImpl.findAll("jwt", 1L, "");

        assertEquals(0, result.size());
    }
}