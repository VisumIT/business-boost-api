/*
* Author: Kaique
* classe para devolver os recursos da empresa
* */

package com.visumIT.Business.boost.resource;

import java.util.ArrayList;
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

import com.visumIT.Business.boost.DTO.EmpresaDTO;
import com.visumIT.Business.boost.DTO.RepresentanteDTO;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.RepresentanteRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;

@RestController
@RequestMapping("/empresa")
public class EmpresaResource {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private RepresentanteRepository representanteRepository;

//  objeto servira para dar retorno ao front sem expor a senha 
	private EmpresaDTO dto = new EmpresaDTO();

	@Autowired
	private TelefoneRepository telefoneRepository;

	@GetMapping
	public List<EmpresaDTO> getEmpresas() {
		List<Empresa> empresas = empresaRepository.findAll();
		return dto.toEmpresasDTO(empresas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEmpresa(@PathVariable Long id) {
		Optional<Empresa> empresaProcurada = empresaRepository.findById(id);
		if (empresaProcurada.isPresent()) {
			EmpresaDTO dtoProcurada = dto.toEmpresaDTO(empresaProcurada.get());
			return ResponseEntity.ok().body(dtoProcurada);
		}

		return empresaProcurada.isPresent() ? ResponseEntity.ok(empresaProcurada.get())
				: ResponseEntity.notFound().build();
	}

	//retorna representantes de um empresa
	@GetMapping("/{id}/representantes")
	public ResponseEntity<?> getRepresentantes(@PathVariable Long id) {
		Optional<Empresa> empresaProcurada = empresaRepository.findById(id);

		if (empresaProcurada.isPresent()) {
			RepresentanteDTO representanteDTO = new RepresentanteDTO();
			List<RepresentanteDTO> representantesDTO = representanteDTO.toRepresentantesDTO(empresaProcurada.get()
					.getRepresentantes());

			return ResponseEntity.ok().body(representantesDTO);

		}
		return ResponseEntity.notFound().build();
	}

	// cadastro da empresa
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveEmpresa(@Valid @RequestBody Empresa empresa, BindingResult bindingResult) {
		// verifica se o email já está cadastrado
		if (empresaRepository.existsByEmail(empresa.getEmail())) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "E-mail allready in use").toString());

			// verifica se o CNPJ já está cadastrado
		} else if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "CNPJ allready in use").toString());

		}else if(empresa.getSenha().length()<8){
			return ResponseEntity.badRequest().body(new JSONObject().put("message",
					"Password must be at least 8 characters").toString());
		}
		else if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));

		} else {
			Empresa e = empresaRepository.save(empresa);
			for (Telefone tel : e.getTelefone()) {
				tel.setEmpresa(e);
				telefoneRepository.save(tel);
			}

			EmpresaDTO dtoProcurada = dto.toEmpresaDTO(e);
			return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
		}
	}

	// cadastro de novo representante
	@PostMapping("/{id}/novo-representante")
	public ResponseEntity<?> saveRepresentante(@Valid @RequestBody Representante representante, @PathVariable Long id, BindingResult bindingResult ){
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
			}else {
				Representante r = representanteRepository.save(representante);
				Optional<Empresa> empresa = empresaRepository.findById(id);
				Empresa emp = new Empresa();
				
				if(empresa.isPresent()) {
					List<Empresa> empresas = new ArrayList<>();
					empresas.add(emp.optionalToEmpresa(empresa));
					representante.setEmpresas(empresas);
					
					for (Telefone tel : r.getTelefone()) {
						tel.setRepresentante(r);
						telefoneRepository.save(tel);
					}
					
					RepresentanteDTO representanteDTO = new RepresentanteDTO();
					RepresentanteDTO dtoProcurada = representanteDTO.toRepresentanteDTO(r);
					return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
				
				}else return ResponseEntity.badRequest().build();

				

			}
		}

	//associar representante já cadastrado
	@PutMapping("/{id_empresa}/representante/{id_representante}")
	public ResponseEntity<?> addRepresentante(@PathVariable Long id_empresa, @PathVariable Long id_representante){
		if(empresaRepository.existsById(id_empresa) && representanteRepository.existsById(id_representante) ) {
			Optional <Empresa> empresaOptional = empresaRepository.findById(id_empresa);
			Optional<Representante> representanteOptional = representanteRepository.findById(id_representante);
			//adicionar novo representante a lista de representantes da empresa
			List <Representante> representantes = empresaOptional.get().getRepresentantes();
			representantes.add(representanteOptional.get());
			empresaOptional.get().setRepresentantes(representantes);
			Empresa empresa = new Empresa();
			empresaRepository.save(empresa.optionalToEmpresa(empresaOptional));
			
			//adicionar nova empresa a lista de empresas do representante
			List <Empresa> empresas = representanteOptional.get().getEmpresas();
			empresas.add(empresaOptional.get());
			representanteOptional.get().setEmpresas(empresas);
			Representante representante = new Representante();
			representanteRepository.save(representante.optionalToRepresentante(representanteOptional));
			
			return ResponseEntity.ok().build();
		}else return ResponseEntity.notFound().build();	
	}	
	
	//desassociar representante
	@PutMapping("/{id_empresa}/desassociar-representante/{id_representante}")
	public ResponseEntity<?> removeRepresentante(@PathVariable Long id_empresa, @PathVariable Long id_representante) {
		if(empresaRepository.existsById(id_empresa) && representanteRepository.existsById(id_representante) ) {
			Optional <Empresa> empresaOptional = empresaRepository.findById(id_empresa);
			Optional<Representante> representanteOptional = representanteRepository.findById(id_representante);
			//pegando lista de representantes para preservar outras associações da empresa
			List <Representante> representantes = empresaOptional.get().getRepresentantes();
			representantes.remove(representanteOptional.get());
			empresaOptional.get().setRepresentantes(representantes);
			Empresa empresa = new Empresa();
			empresaRepository.save(empresa.optionalToEmpresa(empresaOptional));
			
			//pegando lista de empresas para preservar outras associações do representante
			List <Empresa> empresas = representanteOptional.get().getEmpresas();
			empresas.remove(empresaOptional.get());
			representanteOptional.get().setEmpresas(empresas);
			Representante representante = new Representante();
			representanteRepository.save(representante.optionalToRepresentante(representanteOptional));
			
			return ResponseEntity.ok().build();
			
		}else return ResponseEntity.notFound().build();
	}
	
	
	
	// usando o retorno response entity para poder retornar o erro 404 caso tente
	// deletar algo q não existe
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteEmpresa(@PathVariable Long id) {
		if (empresaRepository.existsById(id)) {
			empresaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> update(@RequestBody Empresa empresa, @PathVariable Long id) {
		if (empresaRepository.existsById(id)) {
			// setando o id da empresa para atualizar corretamente
			Optional<Empresa> emp = empresaRepository.findById(id);
			empresa.setId(id);
			/*Garantindo que a senha nunca fique nula*/
			if(empresa.getSenha()==null || empresa.getSenha()=="" ) {
				empresa.setSenha(emp.get().getSenha());
			}
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Telefone tel : empresa.getTelefone()) {
				tel.setEmpresa(empresa);
				tel.setId(emp.get().getTelefone().get(i).getId());
				telefoneRepository.save(tel);
				i++;
			}
			empresa.setId(id);

			empresaRepository.save(empresa);
			return ResponseEntity.ok().body(dto.toEmpresaDTO(empresa));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
