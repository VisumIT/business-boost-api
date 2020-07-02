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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.visumIT.Business.boost.DTO.RepresentativeWithoutCompaniesDTO;
import com.visumIT.Business.boost.enums.Profile;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.PhoneRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.visumIT.Business.boost.security.UserSS;
import com.visumIT.Business.boost.services.UserService;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;

@RestController
@RequestMapping("/representatives")
public class RepresentativeResource {

	@Autowired
	private RepresentativeRepository representativeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;

	@Autowired
	private PhoneRepository phoneRepository;

	private RepresentativeDTO dto = new RepresentativeDTO();
	@Autowired
	private FirebaseStorageService firebase;
	//listar representatives

	@GetMapping
	public List<RepresentativeDTO> getRepresentatives() {
		List<Representative> representatives = representativeRepository.findAll();
		return dto.toRepresentativesDTO(representatives);
	}

	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getRepresentative(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Optional<Representative> representativeProcurado = representativeRepository.findById(id);
		if (representativeProcurado.isPresent()) {
			// CompanyDTO dtoProcurada = dto.toCompanyDTO(companyProcurada.get());
			dto = dto.optionalToRepresentativeDTO(representativeProcurado);

			return ResponseEntity.ok().body(dto);
		} else
			return ResponseEntity.notFound().build();
	}

	// lista de companies de um representative
	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@GetMapping("/{id}/companies")
	public ResponseEntity<?> getCompanies(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Optional<Representative> representativeProcurado = representativeRepository.findById(id);

		if (representativeProcurado.isPresent()) {
			CompanyDTO companyDTO = new CompanyDTO();
			List<Company> emp = representativeProcurado.get().getCompanies();
			List<CompanyDTO> representativesDTO = companyDTO.toCompaniesDTOWithoutEmployees(emp);

			return ResponseEntity.ok().body(representativesDTO);

		} else
			return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveRepresentative(@Valid @RequestBody Representative representative,
			BindingResult bindingResult) {
		// validações
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}
		if (representativeRepository.existsByEmail(representative.getEmail())) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "E-mail allready in use").toString());

			// verifica se o cpf já está cadastrado
		} else if (representativeRepository.existsByCpf(representative.getCpf())) {
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "CPF allready in use").toString());
		}
		// se não houver erros
		else {
			representative.setPassword(bCryptEncoder.encode(representative.getPassword()));
			Profile profile = Profile.REPRESENTATIVE;
			representative.addProfile(profile);
			Representative r = representativeRepository.save(representative);
			for (Phone tel : r.getPhones()) {

				tel.setRepresentative(r);
				phoneRepository.save(tel);
			}
			RepresentativeWithoutCompaniesDTO dtoWC = new RepresentativeWithoutCompaniesDTO();
			dtoWC = dtoWC.toRepresentativeDTO(r);
			return ResponseEntity.status(HttpStatus.CREATED).body(dtoWC);
		}
	}

	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRepresentative(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (representativeRepository.existsById(id)) {
			representativeRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else
			return ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@PutMapping("/{id}")
	public ResponseEntity<?> fullUpdateRepresentative(@Valid @RequestBody Representative representative,
			@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (representativeRepository.existsById(id)) {

			// setando o id do representative para atualizar corretamente
			Optional<Representative> rep = representativeRepository.findById(id);
			representative.setId(id);
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Phone tel : representative.getPhones()) {
				tel.setRepresentative(representative);
				tel.setId(rep.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
			}
			representative.setId(id);
			representativeRepository.save(representative);
			return ResponseEntity.ok().body(new JSONObject().put("message", "User successfully updated").toString());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/* update parcial do representante */
	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdateRepresentative(@PathVariable Long id, Representative bodyRepresentative)
			throws IllegalAccessException {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (representativeRepository.existsById(id)) {
			// bodyRepresentative=validUpdate(bodyRepresentative, id)
			Representative baseRepresentative = new Representative();
			baseRepresentative = baseRepresentative.optionalToRepresentative(representativeRepository.findById(id));
			PartialUpdateValidation validation = new PartialUpdateValidation();
			;
			bodyRepresentative = (Representative) validation.updateFields(bodyRepresentative, baseRepresentative);
			representativeRepository.save(bodyRepresentative);
			dto = dto.toRepresentativeDTO(bodyRepresentative);
			return ResponseEntity.ok().body(dto);
		}
		return ResponseEntity.notFound().build();
	}

	/* upload de foto */
	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@PatchMapping("{id}/photos")
	public ResponseEntity<?> uploadPhotograph(@RequestBody FileUpload file, @PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		ImageValidations imageValidations = new ImageValidations();
		if (!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());
		} else if (representativeRepository.existsById(id)) {
			Optional<Representative> representativeOptional = representativeRepository.findById(id);
			Representative representative = new Representative();
			representative = representative.optionalToRepresentative(representativeOptional);

			if (representative.getPhotograph().isEmpty()) {
				String[] fileName = representativeOptional.get().getPhotograph().split("/");
				firebase.delete(fileName[4]);
			}
			// nome único
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() + file.getFileName();

			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));

			representative.setPhotograph(url.getUrl());
			representativeRepository.save(representative);
			return ResponseEntity.ok().body(representative.getPhotograph());
		}
		return ResponseEntity.badRequest().build();
	}
	@PreAuthorize("hasAnyRole('REPRESENTATIVE')")
	@DeleteMapping("/{id}/photos")
	public ResponseEntity<?> deltePhotograph(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.REPRESENTATIVE) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (representativeRepository.existsById(id)) {
			Representative representative = new Representative();
			representative = representative.optionalToRepresentative(representativeRepository.findById(id));

			String[] fileName = representative.getPhotograph().split("/");

			if (firebase.delete(fileName[4])) {
				representative.setPhotograph(" ");
				representativeRepository.save(representative);

				return ResponseEntity.noContent().build();
			}

		}
		return ResponseEntity.notFound().build();
	}

}
