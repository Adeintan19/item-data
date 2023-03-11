package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.model.TransactionRequest;
import com.buana.itemdata.repository.ProductRepository;
import com.buana.itemdata.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public CustomResponse insertTransaction(TransactionRequest transactionRequest) {
        try {
            TransactionRequest transaction = new TransactionRequest();
            transaction.setProductCode(transactionRequest.getProductCode());
            transaction.setQuantity(transactionRequest.getQuantity());
            transaction.setTransactionId(UUID.randomUUID());
            Optional<Products> byProductCode = productRepository.findByProductCode(transactionRequest.getProductCode());
            if (byProductCode.isEmpty()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "product code tidak ditemukan", null);
            }
            BigDecimal total = byProductCode.get().getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
            transaction.setTotal(total);
            transactionRepository.save(transaction);
            ArrayList<Object> listTransaction = new ArrayList<>();
            listTransaction.add(transaction);
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", listTransaction);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, "ERROR", null);

        }
    }

    @Override
    public CustomResponse addMoreTransaction(TransactionRequest transactionRequest) {
        try {
            List<TransactionRequest> byTransactionId = transactionRepository.findByTransactionId(transactionRequest.getTransactionId());
            if (byTransactionId.isEmpty()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "Transaction id tidak valid", null);
            }
            Optional<Products> byProductCode = productRepository.findByProductCode(transactionRequest.getProductCode());
            if (byProductCode.isEmpty()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "product code not found", null);
            }

            Optional<TransactionRequest> byTransactionIdAndProductCode = transactionRepository.findByTransactionIdAndProductCode(transactionRequest.getTransactionId(), transactionRequest.getProductCode());
            ArrayList<Object> listTransaction = new ArrayList<>();
            if (byTransactionIdAndProductCode.isPresent()) {
                int jumlah = byTransactionIdAndProductCode.get().getQuantity() + transactionRequest.getQuantity();
                BigDecimal total = byProductCode.get().getPrice().multiply(BigDecimal.valueOf(jumlah));
                byTransactionIdAndProductCode.get().setQuantity(jumlah);
                byTransactionIdAndProductCode.get().setTotal(total);
                transactionRepository.save(byTransactionIdAndProductCode.get());
                listTransaction.add(byTransactionIdAndProductCode.get());
            } else {
                TransactionRequest transaction = new TransactionRequest();
                transaction.setProductCode(transactionRequest.getProductCode());
                transaction.setQuantity(transactionRequest.getQuantity());
                transaction.setTransactionId(transactionRequest.getTransactionId());
                BigDecimal total = byProductCode.get().getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
                transaction.setTotal(total);
                transactionRepository.save(transaction);
                listTransaction.add(transaction);
            }
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", listTransaction);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "ERROR", null);
        }
    }

    @Override
    public CustomResponse removeItem(String productCode, UUID transactionId) {
        try {
            Optional<TransactionRequest> byTransactionIdAndProductCode = transactionRepository.findByTransactionIdAndProductCode(transactionId, productCode);
            if (byTransactionIdAndProductCode.isPresent()) {
                transactionRepository.delete(byTransactionIdAndProductCode.get());
                return new CustomResponse(HttpStatus.OK.value(), 200, "delete item success", null);
            } else {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "data not found", null);
            }
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResponse finalizeTransaction(UUID transactionId) {
        try {
            List<TransactionRequest> byTransactionId = transactionRepository.findByTransactionId(transactionId);
            if (byTransactionId.isEmpty()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "Data not found", null);
            }

            BigDecimal totalBelanja = BigDecimal.ZERO;
            for (TransactionRequest trans : byTransactionId) {
                totalBelanja = totalBelanja.add(trans.getTotal());
            }
            return new CustomResponse(HttpStatus.OK.value(), 200, "Success", totalBelanja);

        } catch (Exception e) {
            return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, e.getMessage(), null);
        }
    }
}
