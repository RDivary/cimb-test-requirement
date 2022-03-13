package com.divary.cimbtestrequirement.service;

import com.divary.cimbtestrequirement.dto.request.transaction.ExecuteReq;
import com.divary.cimbtestrequirement.model.TransactionHistory;

import java.util.List;

public interface TransactionService {
    TransactionHistory execute(String jwt, ExecuteReq form);

    TransactionHistory findById(String jwt, Long id);

    List<TransactionHistory> findAll(String jwt, Long idUser, String transactionName);
}
