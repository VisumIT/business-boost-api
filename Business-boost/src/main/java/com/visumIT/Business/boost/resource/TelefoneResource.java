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
import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.RepresentanteRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;

@RestController
@RequestMapping("/telefone")
public class TelefoneResource {

	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private RepresentanteRepository representanteRepository;
	
	@GetMapping
	public List<Telefone> getTelefones() {
		return telefoneRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTelefone(@PathVariable Long id) {
		Optional<?> telefoneProcurado = telefoneRepository.findById(id);
		return telefoneProcurado.isPresent() ? ResponseEntity.ok(telefoneProcurado.get())
				: ResponseEntity.notFound().build();
	}
	//adicionar telefone ao uma empresa
	@PostMapping("/empresa/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Telefone saveTelefoneEmpresa(@Valid @RequestBody Telefone telefone, @PathVariable Long id) {
		Optional<Empresa> emp = empresaRepository.findById(id);
		telefone.setEmpresa(emp.get());
		return telefoneRepository.save(telefone);
	}
	
	//adicionar telefone a um representante
	@PostMapping("/representante/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Telefone saveTelefoneRepresentante(@Valid @RequestBody Telefone telefone, @PathVariable Long id) {
		Optional<Representante> rep = representanteRepository.findById(id);
		telefone.setRepresentante(rep.get());
		return telefoneRepository.save(telefone);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		telefoneRepository.deleteById(id);
	}
	@PutMapping
	public void update (@Valid @RequestBody Telefone telefone) {
		telefoneRepository.save(telefone);
	}
}
