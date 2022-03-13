package com.divary.cimbtestrequirement.repository;

import com.divary.cimbtestrequirement.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findAllByUser_usernameAndTransactionType_transactionCodeEquals(String username, String transactionName);

    List<TransactionHistory> findAllByUser_userIdAndTransactionType_transactionCodeEquals(Long userId, String transactionName);

    List<TransactionHistory> findAllByUser_username(String username);

    List<TransactionHistory> findAllByUser_userId(Long userId);
}
