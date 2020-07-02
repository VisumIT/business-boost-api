package com.visumIT.Business.boost.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Client;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.repository.ClientRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;

@RestController
@RequestMapping("/customers")
public class ClientResource {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	PhoneRepository phoneRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getCustomers(){
		return ResponseEntity.ok().body(clientRepository.findAll());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> saveClient(@Valid @RequestBody Client client,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}
		client = clientRepository.save(client);
		for (Phone tel : client.getPhones()) {
			tel.setClient(client);
			phoneRepository.save(tel);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(client);
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateClient(@Valid @RequestBody Client client, @PathVariable Long id, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}	
		if(clientRepository.existsById(id)) {
			Client opt =clientRepository.findClientById(id);
			client.setCompanies(opt.getCompanies());
			client = clientRepository.save(client);
			return ResponseEntity.status(HttpStatus.CREATED).body(client);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {
		if(clientRepository.existsById(id)) {
			clientRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
