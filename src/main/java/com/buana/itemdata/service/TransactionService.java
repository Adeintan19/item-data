package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Transaction;

import java.util.UUID;

public interface TransactionService {
    CustomResponse insertTransaction(Transaction transactionRequest);
    CustomResponse addMoreTransaction(Transaction transactionRequest);
    CustomResponse removeItem(String productCode, UUID transactionId);
    CustomResponse finalizeTransaction(UUID transactionId);
}
