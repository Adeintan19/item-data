package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.ProductRequest;



public interface ProductService {
    CustomResponse insertProduct (ProductRequest productsRequest);
    CustomResponse inquiryByName (String productsRequest);

}
