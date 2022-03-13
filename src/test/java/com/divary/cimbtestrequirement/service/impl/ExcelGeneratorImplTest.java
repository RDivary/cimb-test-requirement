package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.ErrorException;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.TransactionHistory;
import com.divary.cimbtestrequirement.model.TransactionType;
import com.divary.cimbtestrequirement.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
class ExcelGeneratorImplTest {

    @InjectMocks
    private ExcelGeneratorImpl excelGeneratorImpl;
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

        user = User.builder()
                .userId(1L)
                .username("username")
                .accountNumber("12345678")
                .role(RolesEnum.ROLE_USER)
                .transactionHistories(Collections.singletonList(transactionHistory))
                .build();
    }

    @Test
    void testGenerateTransactionHistory_OK() {
        transactionHistory.setActivityDate(new Date());
        assertNotNull(excelGeneratorImpl.generateTransactionHistory(user));
    }

    @Test
    void testGenerateTransactionHistory_FAILED_whenDateIsNull() {
        assertThrows(ErrorException.class, () -> excelGeneratorImpl.generateTransactionHistory(user));
    }
}