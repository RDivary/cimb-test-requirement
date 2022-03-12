package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.NotFoundException;
import com.divary.cimbtestrequirement.config.handler.exception.UnprocessableEntity;
import com.divary.cimbtestrequirement.dto.request.transactiontype.TransactionTypeReq;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.TransactionType;
import com.divary.cimbtestrequirement.repository.TransactionTypeRepository;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

@ExtendWith(MockitoExtension.class)
class TransactionTypeServiceImplTest {

    @InjectMocks
    TransactionTypeServiceImpl transactionTypeServiceImpl;
    @Mock
    private TransactionTypeRepository transactionTypeRepository;
    @Mock
    private JwtUtils jwtUtils;

    @BeforeAll
    static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader())
                .setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.ERROR);
    }

    @Test
    void testCreate_OK() {
        TransactionType expected = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX001")
                .transactionName("Transaction001")
                .active(true)
                .build();
        when(transactionTypeRepository.save(any(TransactionType.class))).thenReturn(expected);

        TransactionTypeReq transactionTypeReq = TransactionTypeReq.builder()
                .transactionCode("TRX001")
                .transactionName("Transaction001")
                .build();
        TransactionType result = transactionTypeServiceImpl.create(transactionTypeReq);
        assertNotNull(result.getTransactionTypeId());
        assertEquals(expected.getTransactionCode(), result.getTransactionCode());
        assertEquals(expected.getTransactionName(), result.getTransactionName());
        assertTrue(result.isActive());
    }

    @Test
    void testFindById_OK() {
        TransactionType expected = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX001")
                .transactionName("Transaction001")
                .active(true)
                .build();
        when(transactionTypeRepository.findById(1L)).thenReturn(Optional.of(expected));
        TransactionType result = transactionTypeServiceImpl.findById(1L);
        assertNotNull(result.getTransactionTypeId());
        assertEquals(expected.getTransactionCode(), result.getTransactionCode());
        assertEquals(expected.getTransactionName(), result.getTransactionName());
        assertTrue(result.isActive());
    }

    @Test
    void testFindById_FAILED_whenTransactionNotFound() {
        when(transactionTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> transactionTypeServiceImpl.findById(1L));
    }

    @ParameterizedTest
    @CsvSource(value = {"true:true:true:ROLE_USER", "false:true:true:ROLE_USER", "true:true:true:ROLE_ADMIN", "false:false:false:ROLE_ADMIN"}, delimiter = ':')
    void testFindAll_OK(boolean param, boolean paramRepo, boolean active, RolesEnum rolesEnum) {
        TransactionType transactionType = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX001")
                .transactionName("Transaction001")
                .active(active)
                .build();
        List<TransactionType> transactionTypes = Arrays.asList(transactionType, transactionType, transactionType);

        when(jwtUtils.getRoleFromJwtToken("header")).thenReturn(rolesEnum.toString());
        when(transactionTypeRepository.findAllByActiveIs(paramRepo)).thenReturn(transactionTypes);

        List<TransactionType> result = transactionTypeServiceImpl.findAll("header", param);
        assertEquals(3, result.size());

    }

    @Test
    void testUpdate_OK() {
        TransactionType expected = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX002")
                .transactionName("Transaction002")
                .active(true)
                .build();

        when(transactionTypeRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(transactionTypeRepository.save(any(TransactionType.class))).thenReturn(expected);

        TransactionTypeReq transactionTypeReq = TransactionTypeReq.builder()
                .transactionCode("TRX001")
                .transactionName("Transaction001")
                .build();
        TransactionType result = transactionTypeServiceImpl.update(1L, transactionTypeReq);

        assertEquals(expected.getTransactionTypeId(), result.getTransactionTypeId());
        assertEquals(expected.getTransactionCode(), result.getTransactionCode());
        assertEquals(expected.getTransactionName(), result.getTransactionName());
        assertTrue(result.isActive());
    }

    @Test
    void testUpdate_FAILED_whenTransactionTypeNotFound() {
        when(transactionTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        TransactionTypeReq form = new TransactionTypeReq();
        assertThrows(NotFoundException.class, () -> transactionTypeServiceImpl.update(1L, form));
    }

    @Test
    void testDelete_OK() {
        TransactionType expected = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX002")
                .transactionName("Transaction002")
                .active(true)
                .build();

        when(transactionTypeRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(transactionTypeRepository.save(any(TransactionType.class))).thenReturn(expected);

        TransactionType result = transactionTypeServiceImpl.delete(1L);

        assertEquals(expected.getTransactionTypeId(), result.getTransactionTypeId());
        assertEquals(expected.getTransactionCode(), result.getTransactionCode());
        assertEquals(expected.getTransactionName(), result.getTransactionName());
        assertFalse(result.isActive());
    }

    @Test
    void testDelete_FAILED_whenTransactionTypeNotFound() {
        when(transactionTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        TransactionTypeReq form = new TransactionTypeReq();
        assertThrows(NotFoundException.class, () -> transactionTypeServiceImpl.update(1L, form));
    }

    @Test
    void testDelete_FAILED_whenTransactionTypeWasDeleted() {
        TransactionType expected = TransactionType.builder()
                .transactionTypeId(1L)
                .transactionCode("TRX002")
                .transactionName("Transaction002")
                .active(false)
                .build();

        when(transactionTypeRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        assertThrows(UnprocessableEntity.class, () -> transactionTypeServiceImpl.delete(1L));
    }
}