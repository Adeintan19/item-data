package com.buana.itemdata.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class TransactionRequest {
    private UUID transactionId;
    private String productCode;
    private int quantity;

}
