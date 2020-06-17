/*
 * Author: Kaique
 * classe para devolver os recursos do representative
 * */


package com.visumIT.Business.boost.resource;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.DTO.CompanyDTO;
import com.visumIT.Business.boost.DTO.RepresentativeDTO;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.viumIT.business.boost.upload.FileUpload;
import com.viumIT.business.boost.upload.FileUploadUrl;
import com.viumIT.business.boost.upload.FirebaseStorageService;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;

@RestController
@RequestMapping("/representatives")
public class RepresentativeResource {
	
	@Autowired
	private RepresentativeRepository representativeRepository;
	
	@Autowired
	private PhoneRepository phoneRepository;
	
	private RepresentativeDTO dto = new RepresentativeDTO();
	
	private FirebaseStorageService firebase = new FirebaseStorageService();
	
	//valida o update de representante
	private Representative validUpdate(Representative bodyRepresentative, Long id) {
		Representative baseRepresentative = bodyRepresentative.optionalToRepresentative(representativeRepository.findById(id));
		bodyRepresentative.setId(id);
		if(bodyRepresentative.getCompanies()== null) {
			bodyRepresentative.setCompanies(baseRepresentative.getCompanies());
		}
		if(bodyRepresentative.getCpf()== null) {
			bodyRepresentative.setCpf(baseRepresentative.getCpf());
		}
		if(bodyRepresentative.getDateOfBirth()== null) {
			bodyRepresentative.setDateOfBirth(baseRepresentative.getDateOfBirth());
		}
		if(bodyRepresentative.getDescription()== null) {
			bodyRepresentative.setDescription(baseRepresentative.getDescription());
		}
		if(bodyRepresentative.getEmail()== null) {
			bodyRepresentative.setEmail(baseRepresentative.getEmail());
		}
		if(bodyRepresentative.getGender()== null) {
			bodyRepresentative.setGender(baseRepresentative.getGender());
		}
		if(bodyRepresentative.getName()== null) {
			bodyRepresentative.setName(baseRepresentative.getName());
		}
		if(bodyRepresentative.getPassword()== null) {
			bodyRepresentative.setPassword(baseRepresentative.getPassword());
		}
		if(bodyRepresentative.getPhones()== null) {
			bodyRepresentative.setPhones(baseRepresentative.getPhones());
		}
		if(bodyRepresentative.getPhotograph()== null) {
			bodyRepresentative.setPhotograph(baseRepresentative.getPhotograph());
		}
	
		return bodyRepresentative;
	}
		
	//listar representatives
	@GetMapping
	public List <RepresentativeDTO> getRepresentatives(){
		List<Representative> representatives = representativeRepository.findAll();
		return dto.toRepresentativesDTO(representatives);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRepresentative(@PathVariable Long id){
		Optional<Representative> representativeProcurado = representativeRepository.findById(id);
		if(representativeProcurado.isPresent()) {
			//CompanyDTO dtoProcurada = dto.toCompanyDTO(companyProcurada.get());
			dto = dto.optionalToRepresentativeDTO(representativeProcurado);
			
			return ResponseEntity.ok().body(dto);
		}else return ResponseEntity.notFound().build();
	}
	//lista de companies de um representative
	@GetMapping("/{id}/companies")
	public ResponseEntity<?> getCompanies(@PathVariable Long id){
		Optional<Representative> representativeProcurado = representativeRepository.findById(id);
		
		if (representativeProcurado.isPresent()) {
			CompanyDTO companyDTO = new CompanyDTO();
			List<Company> emp = representativeProcurado.get().getCompanies();
			List<CompanyDTO> representativesDTO = companyDTO.toCompaniesDTOWithoutEmployees(emp);

			return ResponseEntity.ok().body(representativesDTO);
		
		}else return ResponseEntity.notFound().build();
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveRepresentative(@Valid @RequestBody Representative representative, BindingResult bindingResult) {
		//validações
		//verifica se o email já está cadastrado
		if (representativeRepository.existsByEmail(representative.getEmail())) {
			return ResponseEntity.badRequest().body(new JSONObject()
					.put("message", "E-mail allready in use").toString());
		 
			//verifica se o cpf já está cadastrado	
		}else if(representativeRepository.existsByCpf(representative.getCpf())) {
			return ResponseEntity.badRequest().body(new JSONObject()
					.put("message", "CPF allready in use")
					.toString());
		} else if(bindingResult.hasErrors()){
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		} 
		//se não houver erros
		else {
			Representative r = representativeRepository.save(representative);
			for (Phone tel : r.getPhones()) {
				tel.setRepresentative(r);
				phoneRepository.save(tel);
			}
			
			RepresentativeDTO dtoProcurada = dto.toRepresentativeDTO(r);
			return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRepresentative(@PathVariable Long id){
		if(representativeRepository.existsById(id)) { 
			representativeRepository.deleteById(id); 
			return ResponseEntity.noContent().build(); 
		}
		else return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>  fullUpdateRepresentative(@Valid @RequestBody Representative representative, @PathVariable Long id) {
		if(representativeRepository.existsById(id)) {
			
			//setando o id do representative para atualizar corretamente
			Optional <Representative> rep = representativeRepository.findById(id);
			representative.setId(id);
			int i=0;
			//seta os ids do telefone para poder atualizar
			for (Phone tel : representative.getPhones()) {
				tel.setRepresentative(representative);
			    tel.setId(rep.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
				}
			representative.setId(id);
			representativeRepository.save(representative);
			return ResponseEntity.ok().body(new JSONObject()
					.put("message", "User successfully updated").toString());
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	/*update parcial do representante*/
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdateRepresentative(@PathVariable Long id, Representative bodyRepresentative) {
		
		if(representativeRepository.existsById(id)) {
			bodyRepresentative=validUpdate(bodyRepresentative, id);
			representativeRepository.save(bodyRepresentative);
			dto = dto.toRepresentativeDTO(bodyRepresentative);
			return ResponseEntity.ok().body(dto);
		}
		return ResponseEntity.notFound().build();
	}
	/*upload de foto*/
	@PatchMapping("{id}/photos")
	public ResponseEntity<?> uploadPhotograph(@RequestBody FileUpload file, @PathVariable Long id ) {
		ImageValidations imageValidations = new ImageValidations();
		if(!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());
		}else if(representativeRepository.existsById(id)) {
			Optional<Representative> representativeOptional = representativeRepository.findById(id);
			Representative representative = new Representative();
			representative = representative.optionalToRepresentative(representativeOptional);
			
			if (representative.getPhotograph().isEmpty()) {
				String[] fileName = representativeOptional.get().getPhotograph().split("/");
				firebase.delete(fileName[4]);
			}			
			//nome único
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() +file.getFileName();
			
			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));
			
			
			representative.setPhotograph(url.getUrl());
			representativeRepository.save(representative);
			return ResponseEntity.ok().body(representative.getPhotograph());
		}
		return ResponseEntity.badRequest().build();
	}
	@DeleteMapping("/{id}/photos")
	public ResponseEntity<?> deltePhotograph(@PathVariable Long id){
		if(representativeRepository.existsById(id)) {
			Representative representative = new Representative();
			representative = representative.optionalToRepresentative(representativeRepository.findById(id));
			
			String[] fileName = representative.getPhotograph().split("/");
		
			if(firebase.delete(fileName[4])) {
				representative.setPhotograph(" ");
				representativeRepository.save(representative);
				
				return ResponseEntity.noContent().build();
			}
			
		}
		return ResponseEntity.notFound().build();
	}
	
	
}
