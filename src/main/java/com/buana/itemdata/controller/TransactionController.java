package com.buana.itemdata.controller;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.model.Transaction;
import com.buana.itemdata.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/cart")
public class TransactionController {
    @Autowired
    CustomResponse response;
    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse> insertTransaction(@RequestBody Transaction transaction) {
        try {
            //masuk ke logic service
            CustomResponse responseService = transactionService.insertTransaction(transaction);
            return new ResponseEntity<>(responseService, HttpStatus.resolve(responseService.getHttpCode()));
        } catch (Exception e) {
            CustomResponse response1 = new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),500, e.getMessage(), null);
            return new ResponseEntity<>(response1, HttpStatus.valueOf(response1.getHttpCode()));
        }
    }
}
