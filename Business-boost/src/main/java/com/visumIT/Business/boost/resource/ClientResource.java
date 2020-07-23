package com.visumIT.Business.boost.resource;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.enums.Profile;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Client;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.ClientRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.visumIT.Business.boost.security.UserSS;
import com.visumIT.Business.boost.services.UserService;

@RestController
@RequestMapping("/customers")
public class ClientResource {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	PhoneRepository phoneRepository;
	
	@Autowired
	RepresentativeRepository representativeRepository;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getCustomers() {
		return ResponseEntity.ok().body(clientRepository.findAll());
	}
	
	@GetMapping("/representative")
	public ResponseEntity<?> getCustomersRepresentative() {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		else {
			Long id = user.getId();
			Representative  representative = new Representative();
			representative = representative.optionalToRepresentative(representativeRepository.findById(id));
			List<Company> companies= representative.getCompanies();
			ArrayList<Client> customers = new ArrayList<>();
			for (Company comp : companies) {
				for(Client cli : comp.getCustomers()) {
					customers.add(cli);
				}
			}
			
			return ResponseEntity.ok().body(customers);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> saveClient(@Valid @RequestBody Client client, BindingResult bindingResult) {
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
	public ResponseEntity<?> updateClient(@Valid @RequestBody Client client, @PathVariable Long id,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}
			if (clientRepository.existsById(id)) {
				Client opt = clientRepository.findClientById(id);
				client.setCompanies(opt.getCompanies());
				client = clientRepository.save(client);
				return ResponseEntity.status(HttpStatus.CREATED).body(client);
			} else {
				return ResponseEntity.notFound().build();
			}
		
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {
		if (clientRepository.existsById(id)) {
			clientRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
