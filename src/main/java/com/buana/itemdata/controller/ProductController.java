package com.buana.itemdata.controller;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.service.ProductService;
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
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    CustomResponse response;

    @Autowired
    ProductService productService;

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse> insertProduct(@RequestBody Products products) {
        CustomResponse response = new CustomResponse();
        try {
            //masuk ke logic service
             CustomResponse responseService = productService.insertProduct(products);
            return new ResponseEntity<>(responseService, HttpStatus.resolve(responseService.getHttpCode()));
        } catch (Exception e) {
             return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpCode()));
        }
    }
}
