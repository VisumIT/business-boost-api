package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Product;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.ProductRepository;

@RestController
@RequestMapping("/company")
public class ProductResource {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	// Listar produtos da empresa
	@GetMapping("/{idCompany}/products")
	public ResponseEntity<?> getProductCompany(@PathVariable Long idCompany){
		//List<Product> = productRepository.findAll();
		//companyRepository.findByProduct(idCompany);
		return ResponseEntity.ok( companyRepository.findById(idCompany));
	}
	
	// Listar produto por id 
	@GetMapping("/products/{idProduct}")
	public ResponseEntity<?> getProductById( @PathVariable Long idProduct) {
		Optional<?> information = productRepository.findById(idProduct);
		
		return information.isPresent() ? ResponseEntity.ok(information.get()) : ResponseEntity.notFound().build();
	}
	
	
	// cadastrar produto da empresa
	@PostMapping("/company/{idCompany}/products")
	public ResponseEntity<?> saveProductCompany(@PathVariable Long idCompany ,@Validated @RequestBody Product product) {
		
		Optional<Company> company = companyRepository.findById(idCompany);
		if(company.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		System.out.println(company.get().toString());
		product.setStatus("active");
		product.setSold(0);
		product.setCompany(company.get());
		product = productRepository.save(product);
		
		return ResponseEntity.ok(product);
	}
	
	@PutMapping("/{idCompany}/products")
	public ResponseEntity<?> updateProduct(@PathVariable Long idCompany ,@Validated @RequestBody Product product) {
		
		Optional<Company> company = companyRepository.findById(idCompany);
		if(company.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		System.out.println(company.get().toString());
		
		product.setCompany(company.get());
		
		return ResponseEntity.ok(productRepository.save(product));
	}
	
	
	
	@DeleteMapping("/{idCompany}/products/{idProduct}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long idCompany, @PathVariable Long idProduct) {
		Optional<Company> company = companyRepository.findById(idCompany);
		if(company.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		productRepository.deleteById(idProduct);
		
		return ResponseEntity.ok(null);
	}
}
