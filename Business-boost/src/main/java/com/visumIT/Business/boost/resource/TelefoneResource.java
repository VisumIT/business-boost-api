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

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;

@RestController
@RequestMapping("empresa/telefone")
public class TelefoneResource {

	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;

	@GetMapping
	public List<Telefone> getTelefone() {
		return telefoneRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTelefone(@PathVariable Long id) {
		Optional<?> telefoneProcurado = telefoneRepository.findById(id);
		return telefoneProcurado.isPresent() ? ResponseEntity.ok(telefoneProcurado.get())
				: ResponseEntity.notFound().build();
	}
	//adicionar telefone ao uma empresa
	@PostMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Telefone gravar(@Valid @RequestBody Telefone telefone, @PathVariable Long id) {		
		Optional<Empresa> emp = empresaRepository.findById(id);
		telefone.setEmpresa(emp.get());
		return telefoneRepository.save(telefone);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		telefoneRepository.deleteById(id);
	}
	@PutMapping
	public void atualizar (@Valid @RequestBody Telefone telefone) {
		telefoneRepository.save(telefone);
	}
}
