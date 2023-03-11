package com.buana.itemdata.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductRequest {
    private String productCode;
    private String productName;
    private BigDecimal price;
}
