package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
}
