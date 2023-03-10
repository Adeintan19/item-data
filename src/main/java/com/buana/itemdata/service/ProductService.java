package com.buana.itemdata.service;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.Products;



public interface ProductService {
    CustomResponse insertProduct (Products productsRequest);
    CustomResponse inquiryByName (String productsRequest);

}
