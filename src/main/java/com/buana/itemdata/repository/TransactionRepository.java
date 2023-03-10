package com.buana.itemdata.repository;

import com.buana.itemdata.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List <Transaction> findByTransactionId(UUID transactionId);
    Optional<Transaction> findByTransactionIdAndProductCode(UUID transactionId, String productCode);
}
