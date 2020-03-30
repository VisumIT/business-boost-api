package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.DTO.RepresentanteDTO;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.RepresentanteRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;

@RestController
@RequestMapping("/representante")
public class RepresentanteResource {
	
	@Autowired
	private RepresentanteRepository representanteRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	private RepresentanteDTO dto = new RepresentanteDTO();
	
	
	//listar representantes
	@GetMapping
	public List <RepresentanteDTO> getRepresentantes(){
		List<Representante> representantes = representanteRepository.findAll();
		return dto.toRepresentantesDTO(representantes);
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
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> gravar(@Valid @RequestBody Representante representante, BindingResult bindingResult) {
		//validações
		//verifica se o email já está cadastrado
		if (representanteRepository.existsByEmail(representante.getEmail())) {
			return ResponseEntity.badRequest().body(new JSONObject()
					.put("message", "E-mail allready in use").toString());
		 
			//verifica se o cpf já está cadastrado	
		}else if(representanteRepository.existsByCpf(representante.getCpf())) {
			return ResponseEntity.badRequest().body(new JSONObject()
					.put("message", "CPF allready in use")
					.toString());
		} else if(bindingResult.hasErrors()){
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		} 
		//se não houver erros
		else {
			Representante r = representanteRepository.save(representante);
			for (Telefone tel : r.getTelefone()) {
				tel.setRepresentante(r);
				telefoneRepository.save(tel);
			}
			
			RepresentanteDTO dtoProcurada = dto.toRepresentanteDTO(r);
			return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		if(representanteRepository.existsById(id)) { 
			representanteRepository.deleteById(id); 
			return ResponseEntity.noContent().build(); 
		}
		else return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>  atualizar(@Valid @RequestBody Representante representante, @PathVariable Long id) {
		if(representanteRepository.existsById(id)) {
			
			//setando o id do representante para atualizar corretamente
			Optional <Representante> rep = representanteRepository.findById(id);
			representante.setId(id);
			int i=0;
			//seta os ids do telefone para poder atualizar
			for (Telefone tel : representante.getTelefone()) {
				tel.setRepresentante(representante);
			    tel.setId(rep.get().getTelefone().get(i).getId());
				telefoneRepository.save(tel);
				i++;
				}
			representante.setId(id);
			representanteRepository.save(representante);
			return ResponseEntity.ok().body(new JSONObject()
					.put("message", "User successfully updated").toString());
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	
}
