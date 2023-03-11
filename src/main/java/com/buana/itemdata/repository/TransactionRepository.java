package com.buana.itemdata.repository;

import com.buana.itemdata.model.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRequest, Integer> {
    List <TransactionRequest> findByTransactionId(UUID transactionId);
    Optional<TransactionRequest> findByTransactionIdAndProductCode(UUID transactionId, String productCode);
}
