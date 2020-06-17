/*
* Author: Kaique
* classe para devolver os recursos da company
* */

package com.visumIT.Business.boost.resource;

import java.util.ArrayList;
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
import com.visumIT.Business.boost.DTO.CompanyWithoutEmployeesDTO;
import com.visumIT.Business.boost.DTO.RepresentativeDTO;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.viumIT.business.boost.upload.FileUpload;
import com.viumIT.business.boost.upload.FileUploadUrl;
import com.viumIT.business.boost.upload.FirebaseStorageService;

@RestController
@RequestMapping("/companies")
public class CompanyResource {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RepresentativeRepository representativeRepository;

	@Autowired
	private FirebaseStorageService firebase;

//  objeto servira para dar retorno ao front sem expor a password 
	private CompanyDTO dto = new CompanyDTO();

	@Autowired
	private PhoneRepository phoneRepository;

	// imagem
	private String standardImage = "https://firebasestorage.googleapis.com/v0/b/teste-ds3-5ded5.appspot.com/o/teste.jpg?alt=media&token=3e676fe1-6744-416f-a550-dcb07b57c1b8";

	private FileUpload file = new FileUpload();

	private ArrayList<String> allowedExtensions = new ArrayList<>();

	// valida alguns campos para cadastro da company
	private String validateCompany(Company company) {
		if (companyRepository.existsByEmail(company.getEmail())) {
			return "E-mail allready in use";
		} else if (companyRepository.existsByCnpj(company.getCnpj())) {
			return "CNPJ allready in use";
		} else if (company.getPassword().length() < 8) {
			return "Password must be at least 8 characters";
		} else {
			return "valid";
		}
	}

	// valida alguns campos de representantes para cadastro
	private String validateRepresentative(Representative representative) {
		if (representativeRepository.existsByEmail(representative.getEmail())) {
			return "E-mail allready in use";
		} else if (representativeRepository.existsByCpf(representative.getCpf())) {
			return "CPF allready in use";
		} else if (representative.getPassword().length() < 8) {
			return "Password must be at least 8 characters";
		} else {
			return "valid";
		}
	}

	// lista todas as empresas
	@GetMapping
	public List<CompanyWithoutEmployeesDTO> getCompanies() {
		List<Company> companies = companyRepository.findAll();
		CompanyWithoutEmployeesDTO companiesWithout = new CompanyWithoutEmployeesDTO();
		return companiesWithout.toCompaniesDTO(companies);
	}

	// detalhes da empresa, está rota deve ser protegida
	@GetMapping("/{id}")
	public ResponseEntity<?> getCompany(@PathVariable Long id) {
		Optional<Company> companyProcurada = companyRepository.findById(id);

		return companyProcurada.isPresent() ? ResponseEntity.ok(companyProcurada.get())
				: ResponseEntity.notFound().build();
	}

	// retorna representatives de um company
	@GetMapping("/{id}/representatives")
	public ResponseEntity<?> getRepresentatives(@PathVariable Long id) {
		Optional<Company> companyProcurada = companyRepository.findById(id);
		if (companyProcurada.isPresent()) {
			RepresentativeDTO representativeDTO = new RepresentativeDTO();
			List<RepresentativeDTO> representativesDTO = representativeDTO
					.toRepresentativesDTO(companyProcurada.get().getRepresentatives());
			return ResponseEntity.ok().body(representativesDTO);
		}
		return ResponseEntity.notFound().build();
	}

	// cadastro da company
	@PostMapping
	public ResponseEntity<?> saveCompany(@Valid @RequestBody Company company, BindingResult bindingResult) {
		if (!validateCompany(company).contains("valid")) {
			String message = validateCompany(company);
			return ResponseEntity.badRequest().body(new JSONObject().put("message", message).toString());
		}
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));

		} else {
			// ImageResource imageResource = new ImageResource();
			company.setLogo(standardImage);
			Company e = companyRepository.save(company);
			for (Phone tel : e.getPhones()) {
				tel.setCompany(e);
				phoneRepository.save(tel);
			}

			CompanyDTO dtoProcurada = dto.toCompanyDTO(e);
			return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
		}
	}

	// cadastro de novo representative
	@PostMapping("/{id}/novo-representative")
	public ResponseEntity<?> saveRepresentative(@Valid @RequestBody Representative representative,
			@PathVariable Long id, BindingResult bindingResult) {
		// validações
		if (!validateRepresentative(representative).contains("valid")) {
			String message = validateRepresentative(representative);
			return ResponseEntity.badRequest().body(new JSONObject().put("message", message).toString());

		} else if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));

		} else {
			// Representative r = representativeRepository.save(representative);
			Optional<Company> company = companyRepository.findById(id);
			Company emp = new Company();

			if (company.isPresent()) {
				List<Company> companies = new ArrayList<>();
				companies.add(emp.optionalToCompany(company));
				representative.setCompanies(companies);

				for (Phone tel : representative.getPhones()) {
					tel.setRepresentative(representative);
					phoneRepository.save(tel);
				}

				RepresentativeDTO representativeDTO = new RepresentativeDTO();
				RepresentativeDTO dtoWanted = representativeDTO.toRepresentativeDTO(representative);
				return ResponseEntity.status(HttpStatus.CREATED).body(dtoWanted);

			} else
				return ResponseEntity.badRequest().build();

		}
	}

	// associar representative já cadastrado
	@PatchMapping("/{id_company}/representatives/{id_representative}")
	public ResponseEntity<?> addRepresentative(@PathVariable Long id_company, @PathVariable Long id_representative) {
		if (companyRepository.existsById(id_company) && representativeRepository.existsById(id_representative)) {
			Optional<Company> companyOptional = companyRepository.findById(id_company);
			Optional<Representative> representativeOptional = representativeRepository.findById(id_representative);
			// adicionar novo representative a lista de representatives da company
			List<Representative> representatives = companyOptional.get().getRepresentatives();
			representatives.add(representativeOptional.get());
			companyOptional.get().setRepresentatives(representatives);
			Company company = new Company();
			companyRepository.save(company.optionalToCompany(companyOptional));

			// adicionar nova company a lista de companies do representative
			List<Company> companies = representativeOptional.get().getCompanies();
			companies.add(companyOptional.get());
			representativeOptional.get().setCompanies(companies);
			Representative representative = new Representative();
			representativeRepository.save(representative.optionalToRepresentative(representativeOptional));

			return ResponseEntity.ok().build();
		} else
			return ResponseEntity.notFound().build();
	}

	// desassociar representative
	@PatchMapping("/{id_company}/disassociate-representative/{id_representative}")
	public ResponseEntity<?> removeRepresentative(@PathVariable Long id_company, @PathVariable Long id_representative) {
		if (companyRepository.existsById(id_company) && representativeRepository.existsById(id_representative)) {
			Optional<Company> companyOptional = companyRepository.findById(id_company);
			Optional<Representative> representativeOptional = representativeRepository.findById(id_representative);
			// pegando lista de representatives para preservar outras associações da company
			List<Representative> representatives = companyOptional.get().getRepresentatives();
			representatives.remove(representativeOptional.get());
			companyOptional.get().setRepresentatives(representatives);
			Company company = new Company();
			companyRepository.save(company.optionalToCompany(companyOptional));

			// pegando lista de companies para preservar outras associações do
			// representative
			List<Company> companies = representativeOptional.get().getCompanies();
			companies.remove(companyOptional.get());
			representativeOptional.get().setCompanies(companies);
			Representative representative = new Representative();
			representativeRepository.save(representative.optionalToRepresentative(representativeOptional));

			return ResponseEntity.ok().build();

		} else
			return ResponseEntity.notFound().build();
	}

	// usando o retorno response entity para poder retornar o erro 404 caso tente
	// deletar algo q não existe
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
		if (companyRepository.existsById(id)) {
			companyRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// atualização parcial da company
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialCompanyUpdate(@PathVariable Long id, @RequestBody Company bodyCompany)
	throws IllegalAccessException{
		Company baseCompany = new Company();
		baseCompany = baseCompany.optionalToCompany(companyRepository.findById(id));
		PartialUpdateValidation validation = new PartialUpdateValidation();
		
		bodyCompany = (Company)validation.updateFields(bodyCompany, baseCompany);
		
		bodyCompany.setId(id);
		companyRepository.save(bodyCompany);
		dto = dto.toCompanyDTO(bodyCompany);
		return ResponseEntity.ok(dto);
	}

	// atualização completa
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> fullCompanyUpdate(@RequestBody Company company, @PathVariable Long id) {
		if (companyRepository.existsById(id)) {
			// setando o id da company para atualizar corretamente
			Optional<Company> emp = companyRepository.findById(id);
			company.setId(id);
			/* Garantindo que a password nunca fique nula */
			if (company.getPassword() == null || company.getPassword() == "") {
				company.setPassword(emp.get().getPassword());
			}
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Phone tel : company.getPhones()) {
				tel.setCompany(company);
				tel.setId(emp.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
			}

			companyRepository.save(company);
			return ResponseEntity.ok().body(dto.toCompanyDTO(company));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/* #############update de logo da empresa########### */
	@PatchMapping("{id}/logo")
	public ResponseEntity<?> updateLogoCompany(@RequestBody FileUpload file, @PathVariable Long id) {
		ImageValidations imageValidations = new ImageValidations();
		if (!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());

		} else if (companyRepository.existsById(id)) {
			Optional<Company> companyOptional = companyRepository.findById(id);
				
			Company company = new Company();
			company = company.optionalToCompany(companyOptional);
			if(!company.getLogo().isEmpty()) {
				String[] fileName = companyOptional.get().getLogo().split("/");
				firebase.delete(fileName[4]);
			}
						
			//garantindo nome único, função será separada depois
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() +file.getFileName();
			
			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));
			
			company = company.optionalToCompany(companyOptional);
			company.setLogo(url.getUrl());
			companyRepository.save(company);
			return ResponseEntity.ok().body(company.getLogo());
		}
		return ResponseEntity.badRequest().build();
	}

	/* #############delete do logo da empresa########### */
	@DeleteMapping("{id}/logo")
	public ResponseEntity<?> deleteLogo(@PathVariable Long id) {
		if (companyRepository.existsById(id)) {
			Optional<Company> companyOptional = companyRepository.findById(id);
			String logo = companyOptional.get().getLogo();
			String[] fileName = logo.split("/");			
			
			if (firebase.delete(fileName[4])) {
				Company company = new Company();
				company = company.optionalToCompany(companyOptional);
				company.setLogo(" ");
				companyRepository.save(company);
				
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// ******************GETTERS and SETTERS
	public CompanyRepository getCompanyRepository() {
		return companyRepository;
	}

	public void setCompanyRepository(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public RepresentativeRepository getRepresentativeRepository() {
		return representativeRepository;
	}

	public void setRepresentativeRepository(RepresentativeRepository representativeRepository) {
		this.representativeRepository = representativeRepository;
	}

	public FirebaseStorageService getFirebase() {
		return firebase;
	}

	public void setFirebase(FirebaseStorageService firebase) {
		this.firebase = firebase;
	}

	public CompanyDTO getDto() {
		return dto;
	}

	public void setDto(CompanyDTO dto) {
		this.dto = dto;
	}

	public PhoneRepository getPhoneRepository() {
		return phoneRepository;
	}

	public void setPhoneRepository(PhoneRepository phoneRepository) {
		this.phoneRepository = phoneRepository;
	}

	public String getStandardImage() {
		return standardImage;
	}

	public void setStandardImage(String standardImage) {
		this.standardImage = standardImage;
	}

	public FileUpload getFile() {
		return file;
	}

	public void setFile(FileUpload file) {
		this.file = file;
	}

	public ArrayList<String> getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(ArrayList<String> allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

}