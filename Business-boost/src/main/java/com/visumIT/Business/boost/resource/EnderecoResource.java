package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Address;
import com.visumIT.Business.boost.repository.AddressRepository;

@RestController
@RequestMapping("/endereco")
public class EnderecoResource {

	@Autowired
	private AddressRepository addressRepository;

	@GetMapping
	public List<Address> getEnderecos() {
		return addressRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEndereco(@PathVariable Long id) {
		Optional<?> enderecoProcurado = addressRepository.findById(id);
		return enderecoProcurado.isPresent() ? ResponseEntity.ok(enderecoProcurado.get())
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Address gravar(@Valid @RequestBody Address endereco) {
		return addressRepository.save(endereco);
	}
	
	public void excluir(@PathVariable Long id) {
		addressRepository.deleteById(id);
	}
	
	public void atualizar(@Valid @RequestBody Address endereco) {
		addressRepository.save(endereco);
	}

}
