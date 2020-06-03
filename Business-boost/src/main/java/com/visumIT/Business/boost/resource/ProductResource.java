package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Product;
import com.visumIT.Business.boost.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductRepository repository;
	
	@GetMapping
	public List<Product> getProduct(){
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductsById(@PathVariable Long id) {
		Optional<?> information = repository.findById(id);
		
		return information.isPresent() ? ResponseEntity.ok(information.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Product save(@Valid @RequestBody Product product) {
		return repository.save(product);
	}
	
	@PutMapping
	public Product update(@Valid @RequestBody Product product) {
		return repository.save(product);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
