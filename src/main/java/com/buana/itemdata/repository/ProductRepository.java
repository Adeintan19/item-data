package com.buana.itemdata.repository;

import com.buana.itemdata.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    Optional<Products> findByProductCode(String productCode);
    List<Products> findByProductNameContaining(String productName);
}
