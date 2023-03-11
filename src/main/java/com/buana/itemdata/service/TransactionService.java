package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.TransactionRequest;

import java.util.UUID;

public interface TransactionService {
    CustomResponse insertTransaction(TransactionRequest transactionRequest);
    CustomResponse addMoreTransaction(TransactionRequest transactionRequest);
    CustomResponse removeItem(String productCode, UUID transactionId);
    CustomResponse finalizeTransaction(UUID transactionId);
}
