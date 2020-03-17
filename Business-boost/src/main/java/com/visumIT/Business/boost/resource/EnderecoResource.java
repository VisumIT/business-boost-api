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

import com.visumIT.Business.boost.models.Endereco;
import com.visumIT.Business.boost.repository.EnderecoRepository;

@RestController
@RequestMapping("/endereco")
public class EnderecoResource {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@GetMapping
	public List<Endereco> getEnderecos() {
		return enderecoRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEndereco(@PathVariable Long id) {
		Optional<?> enderecoProcurado = enderecoRepository.findById(id);
		return enderecoProcurado.isPresent() ? ResponseEntity.ok(enderecoProcurado.get())
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Endereco gravar(@Valid @RequestBody Endereco endereco) {
		return enderecoRepository.save(endereco);
	}
	
	public void excluir(@PathVariable Long id) {
		enderecoRepository.deleteById(id);
	}
	
	public void atualizar(@Valid @RequestBody Endereco endereco) {
		enderecoRepository.save(endereco);
	}

}
