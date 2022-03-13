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
import com.divary.cimbtestrequirement.service.TransactionService;
import com.divary.cimbtestrequirement.service.TransactionTypeService;
import com.divary.cimbtestrequirement.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final UserService userService;

    private final TransactionTypeService transactionTypeService;

    private final JwtUtils jwtUtils;

    public TransactionServiceImpl(TransactionHistoryRepository transactionHistoryRepository, UserService userService, TransactionTypeService transactionTypeService, JwtUtils jwtUtils) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.userService = userService;
        this.transactionTypeService = transactionTypeService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public TransactionHistory execute(String jwt, ExecuteReq form) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userService.findByUsername(username).orElseThrow(() -> new NotFoundException("username not found"));
        TransactionType transactionType = transactionTypeService.findById(form.getTransactionTypeId());

        if (!transactionType.isActive()) {
            log.info("transaction failed because transaction type {} is not active", transactionType.getTransactionName());
            throw new NotFoundException("transaction type not found");
        }
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .amount(form.getAmount())
                .user(user)
                .transactionType(transactionType)
                .build();

        transactionHistory = transactionHistoryRepository.save(transactionHistory);

        log.info("transaction success with amount {}, transaction type {}, username {}", transactionHistory.getAmount(), transactionHistory.getTransactionType().getTransactionName(), transactionHistory.getUser().getUsername());
        return transactionHistory;
    }

    @Override
    public TransactionHistory findById(String jwt, Long id) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        TransactionHistory transactionHistory = findById(id);
        String role = jwtUtils.getRoleFromJwtToken(jwt);

        if (!RolesEnum.ROLE_ADMIN.toString().equalsIgnoreCase(role)
                && !transactionHistory.getUser().getUsername().equalsIgnoreCase(username)) {
            log.error("access denied");
            throw new ForbiddenException("you cannot access");
        }
        return transactionHistory;
    }

    @Override
    public List<TransactionHistory> findAll(String jwt, Long idUser, String transactionName) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        String role = jwtUtils.getRoleFromJwtToken(jwt);

        List<TransactionHistory> transactionHistories;

        if (RolesEnum.ROLE_ADMIN.toString().equalsIgnoreCase(role)) {
            if (Strings.isBlank(transactionName))
                transactionHistories = transactionHistoryRepository.findAllByUser_userId(idUser);
            else
                transactionHistories = transactionHistoryRepository.findAllByUser_userIdAndTransactionType_transactionCodeEquals(idUser, transactionName);
        } else if (RolesEnum.ROLE_USER.toString().equalsIgnoreCase(role)) {
            if (Strings.isBlank(transactionName))
                transactionHistories = transactionHistoryRepository.findAllByUser_username(username);
            else
                transactionHistories = transactionHistoryRepository.findAllByUser_usernameAndTransactionType_transactionCodeEquals(username, transactionName);
        } else {
            transactionHistories = new ArrayList<>();
        }

        return transactionHistories;
    }

    private TransactionHistory findById(Long id) {
        return transactionHistoryRepository.findById(id).orElseThrow(() -> new NotFoundException("transaction not found"));
    }
}
