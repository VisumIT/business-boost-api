package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.RepresentanteRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;

@RestController
@RequestMapping("/representante")
public class RepresentanteResource {
	
	@Autowired
	private RepresentanteRepository representanteRepository;
	
	private TelefoneRepository telefoneRepository;
	
	//listar representantes
	@GetMapping
	public List <Representante> getRepresentantes(){
		return representanteRepository.findAll();
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRepresentate(@PathVariable Long id){
		Optional<Representante> representanteProcurada = representanteRepository.findById(id);
		if(representanteProcurada.isPresent()) {
			//EmpresaDTO dtoProcurada = dto.toEmpresaDTO(empresaProcurada.get());
			return ResponseEntity.ok().body(representanteProcurada);
		}else return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> gravar(@Valid @RequestBody Representante representante, BindingResult bindingResult) {
		//verifica se o email já está cadastrado
		if(representanteRepository.existsByEmail(representante.getEmail())) {
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "E-mail allready in use").toString());
		
		}else if(representanteRepository.existsByCpf(representante.getCpf())){
			return ResponseEntity.badRequest().body(new JSONObject()
					.put("message", "CPF allready in use")
					.toString());
		}else {
			Representante r = representanteRepository.save(representante);
			for (Telefone tel : r.getTelefone()) {
				tel.setRepresentante(r);
				telefoneRepository.save(tel);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(representante);
		}
	}
	
}
