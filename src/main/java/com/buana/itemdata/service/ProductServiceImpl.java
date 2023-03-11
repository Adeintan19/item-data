package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.ProductRequest;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public CustomResponse insertProduct(ProductRequest productsRequest) {
        try {
            Optional<Products> byProductCode = productRepository.findByProductCode(productsRequest.getProductCode());
            if (byProductCode.isPresent()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "Product code already exist", null);
            }
            //mapping data
            Products product = new Products();
            product.setProductCode(productsRequest.getProductCode());
            product.setProductName(productsRequest.getProductName());
            product.setPrice(productsRequest.getPrice());
            product.setCreatedAt(new Date());
            //save processing
            productRepository.save(product);
            //return success
            log.info("insert product succesfully");
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", product);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, "ERROR", null);

        }
    }

    @Override
    public CustomResponse inquiryByName(String productsNameRequest) {
        try {
            List<Products> byProductNameContaining = productRepository.findByProductNameContaining(productsNameRequest);
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", byProductNameContaining);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, "ERROR", null);
        }

    }
}
