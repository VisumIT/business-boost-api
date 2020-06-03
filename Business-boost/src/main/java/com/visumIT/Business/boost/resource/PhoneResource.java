package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;

@RestController
@RequestMapping("/phones")
public class PhoneResource {

	@Autowired
	private PhoneRepository phoneRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RepresentativeRepository representativeRepository;
	
	@GetMapping
	public List<Phone> getPhones() {
		return phoneRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPhone(@PathVariable Long id) {
		Optional<?> telefoneProcurado = phoneRepository.findById(id);
		return telefoneProcurado.isPresent() ? ResponseEntity.ok(telefoneProcurado.get())
				: ResponseEntity.notFound().build();
	}
	//adicionar telefone ao uma company
	@PostMapping("/companies/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Phone savePhoneCompany(@Valid @RequestBody Phone phone, @PathVariable Long id) {
		Optional<Company> emp = companyRepository.findById(id);
		phone.setCompany(emp.get());
		return phoneRepository.save(phone);
	}
	
	//adicionar telefone a um representante
	@PostMapping("/representatives/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Phone savePhoneRepresentante(@Valid @RequestBody Phone phone, @PathVariable Long id) {
		Optional<Representative> rep = representativeRepository.findById(id);
		phone.setRepresentative(rep.get());
		return phoneRepository.save(phone);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		phoneRepository.deleteById(id);
	}
	@PutMapping
	public void update (@Valid @RequestBody Phone phone) {
		phoneRepository.save(phone);
	}
}
