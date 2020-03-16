package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Endereco;
import com.visumIT.Business.boost.repository.EnderecoRepository;

@RestController
@RequestMapping("/endereco")
public class EnderecoResource {

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@GetMapping
	public List <Endereco> getEnderecos() { 
		return enderecoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEndereco(@PathVariable Long id){
		Optional<?>enderecoProcurado = enderecoRepository.findById(id);
		return enderecoProcurado.isPresent() ?
				ResponseEntity.ok(enderecoProcurado.get()) :
					ResponseEntity.notFound().build();
	}
	
	
}
