package com.buana.itemdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "product")
public class Products implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @JsonIgnore
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @Column(name = "update_at")
    private Date updateAt;
}
