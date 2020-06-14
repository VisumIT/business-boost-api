package com.visumIT.Business.boost.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.visumIT.Business.boost.models.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

	
	
}
