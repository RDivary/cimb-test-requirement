package com.divary.cimbtestrequirement.service.impl;

import com.divary.cimbtestrequirement.config.handler.exception.NotFoundException;
import com.divary.cimbtestrequirement.config.handler.exception.UnprocessableEntity;
import com.divary.cimbtestrequirement.dto.request.transactiontype.TransactionTypeReq;
import com.divary.cimbtestrequirement.enums.RolesEnum;
import com.divary.cimbtestrequirement.model.TransactionType;
import com.divary.cimbtestrequirement.repository.TransactionTypeRepository;
import com.divary.cimbtestrequirement.security.jwt.JwtUtils;
import com.divary.cimbtestrequirement.service.TransactionTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    private final JwtUtils jwtUtils;

    public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository, JwtUtils jwtUtils) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public TransactionType create(TransactionTypeReq form) {

        TransactionType transactionType = TransactionType.builder()
                .transactionCode(form.getTransactionCode())
                .transactionName(form.getTransactionName())
                .active(true)
                .build();

        transactionType = transactionTypeRepository.save(transactionType);

        log.info("success save with transaction id={}, transaction code={}, transaction name={}", transactionType.getTransactionTypeId(), transactionType.getTransactionCode(), transactionType.getTransactionName());
        return transactionType;
    }

    @Override
    public TransactionType findById(Long id) {
        return transactionTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("transaction type not found"));
    }

    @Override
    public List<TransactionType> findAll(String header, boolean isActive) {
        String role = jwtUtils.getRoleFromJwtToken(header);
        if (RolesEnum.ROLE_USER.toString().equalsIgnoreCase(role)) isActive = true;

        return transactionTypeRepository.findAllByActiveIs(isActive);
    }

    @Override
    public TransactionType update(Long id, TransactionTypeReq form) {
        TransactionType transactionType = findById(id);
        transactionType.setTransactionCode(form.getTransactionCode());
        transactionType.setTransactionName(form.getTransactionName());

        transactionType = transactionTypeRepository.save(transactionType);
        log.info("success update with transaction id={}, transaction code={}, transaction name={}", transactionType.getTransactionTypeId(), transactionType.getTransactionCode(), transactionType.getTransactionName());
        return transactionType;
    }

    @Override
    public TransactionType delete(Long id) {
        TransactionType transactionType = findById(id);
        if (!transactionType.isActive()) {
            log.error("failed delete transaction type with transactio id {}, because transaction type was deleted ", transactionType.getTransactionTypeId());
            throw new UnprocessableEntity("transaction type was deleted");
        }
        transactionType.setActive(false);
        transactionType = transactionTypeRepository.save(transactionType);
        log.info("success deactivate with transaction id={}, transaction code={}, transaction name={}", transactionType.getTransactionTypeId(), transactionType.getTransactionCode(), transactionType.getTransactionName());
        return transactionType;
    }
}
