package com.tokobuku.nitnot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tokobuku.nitnot.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
