package com.visumIT.Business.boost.resource;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Brand;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;
import com.visumIT.Business.boost.repository.BrandRepository;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Brand;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.repository.BrandRepository;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;

@RestController
@RequestMapping("/companies/{id_company}/brands")
@CrossOrigin
public class BrandResource {

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private FirebaseStorageService firebase;
	private Company company = new Company();
	
	// listar brands de uma company
	@GetMapping
	public ResponseEntity<?> getBrands(@PathVariable(name = "id_company") Long id) {
		if (companyRepository.existsById(id)) {
			Optional<Company> companyOptional = companyRepository.findById(id);
			company = company.optionalToCompany(companyOptional);
			List<Brand> brands = brandRepository.findByCompany(company);

			return ResponseEntity.ok().body(brands);
		}

		return ResponseEntity.notFound().build();

	}

	// adicionar uma brand
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<?> saveBrand(@PathVariable(name = "id_company") Long id, @Valid @RequestBody Brand brand,
			BindingResult bindingResult) {
		/* garantindo que a company exista */
		if (companyRepository.existsById(id)) {
			Optional<Company> companyOptional = companyRepository.findById(id);
			company = company.optionalToCompany(companyOptional);

			List<Brand> brands = company.getBrand();
			brands.add(brand);
			brand.setCompany(company);
			company.setBrand(brands);
			brandRepository.save(brand);
			companyRepository.save(company);

			return ResponseEntity.status(HttpStatus.CREATED).body(brands);

		} else if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	// atualização parcial da brand
	@PatchMapping("{id_brand}")
	public ResponseEntity<?> partialUpdateBrand(@PathVariable(name = "id_company") Long id_company,
			@PathVariable(name = "id_brand") Long id_brand, Brand bodyBrand) 
					throws IllegalAccessException{
		if (companyRepository.existsById(id_company) && brandRepository.existsById(id_brand)) {
			// garantir que a marca pertence a empresa
			if (brandRepository.existsByCompany(company)) {
				//brand = validUpdate(brand, id_brand);
				Brand baseBrand = new Brand();
				baseBrand = baseBrand.optionalToBrand(brandRepository.findById(id_brand));
				PartialUpdateValidation validation = new PartialUpdateValidation();
				bodyBrand = (Brand)validation.updateFields(bodyBrand, baseBrand);
				brandRepository.save(bodyBrand);
				return ResponseEntity.ok(bodyBrand);
			}
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.badRequest().build();
	}

	// Atualização completa do brand
	@PutMapping("/{id_brand}")
	public ResponseEntity<?> fullUpdateBrand(@PathVariable(name = "id_company") Long id_company,
			@PathVariable(name = "id_brand") Long id_brand, @RequestBody Brand brand, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		} else if (companyRepository.existsById(id_company) && brandRepository.existsById(id_brand)) {
			Optional<Company> companyOptional = companyRepository.findById(id_company);
			company = company.optionalToCompany(companyOptional);
			brand.setCompany(company);

			List<Brand> brands = company.getBrand();
			brands.add(brand);
			company.setBrand(brands);
			/* garantir que o id da brand não mude */
			brand.setId(id_brand);
			brandRepository.save(brand);
			companyRepository.save(company);

			return ResponseEntity.status(HttpStatus.CREATED).body(brand);

		} else {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "Brand or Company not found").toString());
		}

	}

	@DeleteMapping("{id_brand}")
	public ResponseEntity<?> deleteBrand(@PathVariable(name = "id_company") Long id_company,
			@PathVariable(name = "id_brand") Long id_brand) {
		if (companyRepository.existsById(id_company) && brandRepository.existsById(id_brand)) {
			brandRepository.deleteById(id_brand);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "Brand or Company not found").toString());
		}
	}

	/* ######################### upload do logo ############################# */

	@PatchMapping("{id_brand}/logo")
	public ResponseEntity<?> updateLogoCompany(@RequestBody FileUpload file, @PathVariable(name="id_brand") Long id_brand,
			@PathVariable(name="id_company")Long id_company) {
		ImageValidations imageValidations = new ImageValidations();
		company = company.optionalToCompany(companyRepository.findById(id_company));
		if (!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());
			
		} else if (brandRepository.existsByCompany(company)) {
			Optional<Brand> brandOptional = brandRepository.findById(id_brand);
			Brand brand = new Brand();
			brand = brand.optionalToBrand(brandOptional);
				
			
			if (!brand.getLogo().isEmpty()) {
				String[] fileName = brandOptional.get().getLogo().split("/");
				firebase.delete(fileName[4]);
			}

			//garantindo nome único, função será separada depois
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() +file.getFileName();
			
			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));
				

			brand.setLogo(url.getUrl());
			brandRepository.save(brand);
			return ResponseEntity.ok().body(brand.getLogo());
		}
		return ResponseEntity.badRequest().build();
	}




	// deletar logo da marca
	@DeleteMapping("{id_brand}/logo")
	public ResponseEntity<?> deleteLogo(@PathVariable(name = "id_brand") Long id_brand, @PathVariable(name = "id_company") Long id_company){
		company = company.optionalToCompany(companyRepository.findById(id_company));
		if (brandRepository.existsByCompany(company)) {
			Brand brand = new Brand();
			brand = brand.optionalToBrand(brandRepository.findById(id_brand));
			
			String[] fileName = brand.getLogo().split("/");

			if (firebase.delete(fileName[4])) {
				brand.setLogo(" ");
				brandRepository.save(brand);

				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
