package com.visumIT.Business.boost.resource;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;


@RestController
@RequestMapping("/company")
@CrossOrigin
public class ProductResource {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private FirebaseStorageService firebase;
	
	// Listar produtos da empresa
	@GetMapping("/{idCompany}/products")
	public ResponseEntity<?> getProductCompany(@PathVariable Long idCompany){
		Optional<Company> product = companyRepository.findById(idCompany);
		
		if(!product.isPresent()) {
			return ResponseEntity.notFound().build();
		}
			
		return ResponseEntity.ok( product.get().getProduct());
	}
	
	// Listar produto por id 
	@GetMapping("/products/{idProduct}")
	public ResponseEntity<?> getProductById( @PathVariable Long idProduct) {
		Optional<?> information = productRepository.findById(idProduct);
		return information.isPresent() ? ResponseEntity.ok(information.get()) : ResponseEntity.notFound().build();
	}
	
	
	// cadastrar produto da empresa
	
	@PostMapping("/{idCompany}/products")
	public ResponseEntity<?> saveProductCompany(
								@PathVariable Long idCompany ,
								@Validated @RequestBody Product product) {
		Optional<Company> company = companyRepository.findById(idCompany);
		
		if(!company.isPresent()) {
			return ResponseEntity.status(400).build();
		}
		
		if(product.getDiscount() > 0.0 && product.getDiscount() < 100.0) {
			Double priceDiscount = product.getPrice() * ( product.getDiscount() / 100);
			product.setDiscontPrice(priceDiscount);
		}else {
			product.setDiscontPrice(0.00);
		}

		product.setTotalPrice(product.getPrice() - product.getDiscontPrice());
		product.setStatus("active");
		product.setSold(0);
		product.setCompany(company.get());
		product = productRepository.save(product);
		
		
		return ResponseEntity.ok(product);
	}
	
	@PutMapping("/{idCompany}/products")
	public ResponseEntity<?> updateProduct(@PathVariable Long idCompany ,@Validated @RequestBody Product product) {
		Optional<Company> company = companyRepository.findById(idCompany);
		
		if(!company.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Double priceDiscount = product.getPrice() * (product.getDiscount() / 100);
		System.out.println(priceDiscount);
		product.setDiscontPrice(priceDiscount);
		product.setTotalPrice(product.getPrice() - product.getDiscontPrice());
		
		product.setCompany(company.get());
		return ResponseEntity.ok(productRepository.save(product));
	}
	
	
	
	@DeleteMapping("/{idCompany}/products/{idProduct}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long idCompany, @PathVariable Long idProduct) {
		Optional<Company> company = companyRepository.findById(idCompany);
		if(company.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		productRepository.deleteById(idProduct);
		
		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/{idCompany}/products/{idProduct}/upload")
	public ResponseEntity<FileUploadUrl> uploadImage(
			@PathVariable Long idCompany, 
			@PathVariable Long idProduct, 
			@RequestBody FileUpload file) {
		
		Company company = companyRepository.findById(idCompany).orElse(null);
		if(company == null) return ResponseEntity.notFound().build();
			
		Product product = productRepository.findById(idProduct).orElse(null);
		if(product == null) return ResponseEntity.notFound().build();
	
		Random random = new Random();
		file.setFileName(file.getFileName() + random.nextInt());
		
		FileUploadUrl url = new FileUploadUrl(firebase.upload(file, file.getFileName()));
		
		product.setImagesUrl(url.getUrl());
		
		productRepository.save(product);
		
		return ResponseEntity.ok(url);
	}
}
