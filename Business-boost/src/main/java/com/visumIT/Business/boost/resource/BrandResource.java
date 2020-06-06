package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Brand;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.BrandRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


@RestController
@RequestMapping("/companies/{id_company}/brands")
public class BrandResource {

	@Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    private
    Company company = new Company();

    //listar brands de uma company
    @GetMapping
    public ResponseEntity<?> getBrands (@PathVariable(name="id_company") Long id ) {
        if(companyRepository.existsById(id)) {
            Optional<Company> companyOptional = companyRepository.findById(id);
            company = company.optionalToCompany(companyOptional);
            List<Brand> brands = brandRepository.findByCompany(company);

            return ResponseEntity.ok().body(brands);
        }

        return ResponseEntity.notFound().build();

    }
    //adicionar uma brand
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> saveBrand(@PathVariable(name="id_company") Long id, @Valid @RequestBody Brand brand, BindingResult bindingResult) {
        /*garantindo que a company exista*/
        if(companyRepository.existsById(id)) {
            Optional<Company> companyOptional = companyRepository.findById(id);
            company = company.optionalToCompany(companyOptional);
            
            List<Brand> brands = company.getBrand();
            brands.add(brand);
            brand.setCompany(company);
            company.setBrand(brands);
            brandRepository.save(brand);
            companyRepository.save(company);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(brands);
            
        }else if(bindingResult.hasErrors()) {
        	return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(brand);
    }
    //Atualizar uma brand
    @PutMapping("/{id_brand}")
    public ResponseEntity<?> updateBrand(@PathVariable(name="id_company")Long id_company,
    		@PathVariable(name="id_brand")Long id_brand, @RequestBody Brand brand, BindingResult bindingResult){
    	
    	if(bindingResult.hasErrors()) {
        	return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
    	}
    	else if(companyRepository.existsById(id_company) && brandRepository.existsById(id_brand)) {
    		Optional<Company> companyOptional = companyRepository.findById(id_company);
    		company = company.optionalToCompany(companyOptional);
    		brand.setCompany(company);
    		
    		List<Brand> brands = company.getBrand();
    		brands.add(brand);
    		company.setBrand(brands);
    		/*garantir que o id da brand não mude*/
    		brand.setId(id_brand);
    		brandRepository.save(brand);
    		companyRepository.save(company);
    		
    		return ResponseEntity.status(HttpStatus.CREATED).body(brand);
    		
    	}else {
    		return ResponseEntity.badRequest().body(new JSONObject().put("message", "Brand or Company not found").toString());
    	}
    	
    }
    @DeleteMapping("{id_brand}")
    public ResponseEntity<?> deleteBrand(@PathVariable(name="id_company")Long id_company,
    		@PathVariable(name="id_brand")Long id_brand){
    	if(companyRepository.existsById(id_company) && brandRepository.existsById(id_brand)) {
    		brandRepository.deleteById(id_brand);
    		return ResponseEntity.noContent().build();
    	}else {
    		return ResponseEntity.badRequest().body(new JSONObject().put("message", "Brand or Company not found").toString());
    	}
    }
    
    
    
}
