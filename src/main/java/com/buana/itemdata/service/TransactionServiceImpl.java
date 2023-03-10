package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.model.Transaction;
import com.buana.itemdata.repository.ProductRepository;
import com.buana.itemdata.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public CustomResponse insertTransaction(Transaction transactionRequest) {
        try{
            Transaction transaction = new Transaction();
            transaction.setProductCode(transactionRequest.getProductCode());
            transaction.setQuantity(transactionRequest.getQuantity());
            transaction.setTransactionId(UUID.randomUUID());
            Optional<Products> byProductCode = productRepository.findByProductCode(transactionRequest.getProductCode());
            if(byProductCode.isEmpty()){
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(),  400, "product code tidak ditemukan", null);
                //return product code tidak ditemukan
            }
            BigDecimal total= byProductCode.get().getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
            transaction.setTotal(total);
            transactionRepository.save(transaction);
            ArrayList<Object> listTransaction = new ArrayList<>();
            listTransaction.add(transaction);
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", listTransaction);
        }catch (Exception e){
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, "ERROR", null);

        }
    }

    @Override
    public CustomResponse addMoreTransaction(Transaction transactionRequest) {
        try{
            return null;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public CustomResponse removeTransaction(int productId) {
        try{
            return null;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public CustomResponse finalizeTransaction(UUID transactionId) {
        try{
            return null;
        }catch (Exception e){
            return null;
        }
    }
}