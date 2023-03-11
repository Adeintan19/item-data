package com.buana.itemdata.controller;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.ProductRequest;
import com.buana.itemdata.model.Products;
import com.buana.itemdata.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse> insertProduct(@RequestBody ProductRequest products) {
        try {
            //masuk ke logic service
             CustomResponse responseService = productService.insertProduct(products);
            return new ResponseEntity<>(responseService, HttpStatus.valueOf(responseService.getHttpCode()));
        } catch (Exception e) {
            CustomResponse response1 = new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),500, e.getMessage(), null);
            return new ResponseEntity<>(response1, HttpStatus.valueOf(response1.getHttpCode()));
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse> searchByName(@RequestParam String productName){
        try{
            CustomResponse responseService = productService.inquiryByName(productName);
            return new ResponseEntity<>(responseService, HttpStatus.valueOf(responseService.getHttpCode()));
        }catch (Exception e){
            CustomResponse response1 = new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),500, e.getMessage(), null);
            return new ResponseEntity<>(response1, HttpStatus.valueOf(response1.getHttpCode()));
        }

    }
}
