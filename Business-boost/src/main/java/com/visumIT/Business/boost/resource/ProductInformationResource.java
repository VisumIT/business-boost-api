package com.visumIT.Business.boost.resource;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.ProductInformation;
import com.visumIT.Business.boost.repository.ProductInformationRepository;

@RestController
@RequestMapping("/products/informations")
public class ProductInformationResource {
	
	@Autowired
	private ProductInformationRepository repository;
	
	@GetMapping
	public List<ProductInformation> getProductsInformations() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductsInformationsById(@PathVariable Long id) {
		Optional<?> information = repository.findById(id);
		
		return information.isPresent() ? ResponseEntity.ok(information.get()) : ResponseEntity.notFound().build();
	}
	
	@PutMapping
	public ProductInformation update(@Valid @RequestBody ProductInformation information) {
		return repository.save(information);
	}
}
