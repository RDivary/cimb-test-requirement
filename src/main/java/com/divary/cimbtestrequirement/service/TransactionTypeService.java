package com.divary.cimbtestrequirement.service;

import com.divary.cimbtestrequirement.dto.request.transactiontype.TransactionTypeReq;
import com.divary.cimbtestrequirement.model.TransactionType;

import java.util.List;

public interface TransactionTypeService {
    TransactionType create(TransactionTypeReq form);

    TransactionType findById(Long id);

    List<TransactionType> findAll(String header, boolean isActive);

    TransactionType update(Long id, TransactionTypeReq form);

    TransactionType delete(Long id);

}
