package com.divary.cimbtestrequirement.repository;

import com.divary.cimbtestrequirement.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    List<TransactionType> findAllByActiveIs(boolean active);
}
