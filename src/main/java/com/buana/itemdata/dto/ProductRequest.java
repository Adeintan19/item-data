package com.buana.itemdata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
@Validated
public class ProductRequest {
    @NotBlank(message = "can't be null or empty")
    @Pattern(regexp= "^[a-zA-Z0-9- ]+$",
            message = "just letters,numbers,space, and character - ")
    private String productCode;

    @NotBlank(message = "can't be null or empty")
    @Pattern(regexp= "^[a-zA-Z0-9- ]+$",
            message = "just letters,numbers,space, and character - ")
    private String productName;

    @NotNull(message = "can't be null or empty")
    private BigDecimal price;
}
