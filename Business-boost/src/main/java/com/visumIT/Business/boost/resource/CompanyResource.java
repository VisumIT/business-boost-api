/*
* Author: Kaique
* classe para devolver os recursos da company
* */

package com.visumIT.Business.boost.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import com.visumIT.Business.boost.DTO.RepresentativeWithoutCompaniesDTO;
import com.visumIT.Business.boost.enums.Profile;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Client;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.ClientRepository;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.visumIT.Business.boost.security.UserSS;
import com.visumIT.Business.boost.services.UserService;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;

@RestController
@RequestMapping("/companies")
public class CompanyResource {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RepresentativeRepository representativeRepository;

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;

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
			throw new ConstraintViolationException("E-mail allready in use", null);
		} else if (companyRepository.existsByCnpj(company.getCnpj())) {
			throw new ConstraintViolationException("cnpj already in use", null);
			// return "CNPJ allready in use";
		} else if (company.getPassword().length() < 8) {
			throw new ConstraintViolationException("Password must be at least 8 characters", null);
		} else {
			return null;
		}
	}

	// valida alguns campos de representantes para cadastro
	private String validateRepresentative(Representative representative) {
		if (representativeRepository.existsByEmail(representative.getEmail())) {
			throw new ConstraintViolationException("E-mail allready in use", null);
		} else if (representativeRepository.existsByCpf(representative.getCpf())) {
			throw new ConstraintViolationException("CPF allready in use", null);
		} else if (representative.getPassword().length() < 8) {
			throw new ConstraintViolationException("Password must be at least 8 characters", null);
		} else {
			return null;
		}
	}

	// lista todas as empresas
	@GetMapping
	public List<CompanyWithoutEmployeesDTO> getCompanies() {
		List<Company> companies = companyRepository.findAll();
		CompanyWithoutEmployeesDTO companiesWithout = new CompanyWithoutEmployeesDTO();
		return companiesWithout.toCompaniesDTO(companies);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCompany(@PathVariable Long id) {
		Optional<Company> companyProcurada = companyRepository.findById(id);

		return companyProcurada.isPresent() ? ResponseEntity.ok(companyProcurada.get())
				: ResponseEntity.notFound().build();
	}

	// retorna representatives de um company
	@GetMapping("/{id}/representatives")
	public ResponseEntity<?> getRepresentatives(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Optional<Company> companyProcurada = companyRepository.findById(id);
		if (companyProcurada.isPresent()) {
			RepresentativeWithoutCompaniesDTO dtoWC = new RepresentativeWithoutCompaniesDTO();
			List<RepresentativeWithoutCompaniesDTO> representativesDTO = dtoWC
					.toRepresentativesDTO(companyProcurada.get().getRepresentatives());
			return ResponseEntity.ok().body(representativesDTO);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}/customers")
	public ResponseEntity<?> getCustomers(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Optional<Company> companyProcurada = companyRepository.findById(id);
		if (companyProcurada.isPresent()) {
			
			List<Client> customers = companyProcurada.get().getCustomers();
			return ResponseEntity.ok().body(customers);
		}
		return ResponseEntity.notFound().build();
	}

	// cadastro da company
	@PostMapping
	public ResponseEntity<?> saveCompany(@Valid @RequestBody Company company, BindingResult bindingResult) {
		try {
			validateCompany(company);
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
			} else {
				// ImageResource imageResource = new ImageResource();
				company.setLogo(standardImage);
				// criptografar senha
				company.setPassword(bCryptEncoder.encode(company.getPassword()));
				Profile profile = Profile.ADMIN;
				company.addProfile(profile);
				Company e = companyRepository.save(company);
				for (Phone tel : e.getPhones()) {
					tel.setCompany(e);
					phoneRepository.save(tel);
				}

				CompanyDTO dtoProcurada = dto.toCompanyDTO(e);
				return ResponseEntity.status(HttpStatus.CREATED).body(dtoProcurada);
			}
		} catch (ConstraintViolationException e) {
			JSONObject jsonObject = null;
			JSONArray jsonArray = new JSONArray();
			jsonObject = new JSONObject().put("Field", e.getCause());
			jsonObject.put("Message", e.getMessage());
			jsonArray.put(jsonObject);
			JSONObject json = new JSONObject().put("Errors", jsonArray);

			return ResponseEntity.badRequest().body(json.toString());
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/{id}/new-client")
	public ResponseEntity<?> saveClient(@Valid @RequestBody Client client, BindingResult bindingResult,@PathVariable Long id){
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}

		if (clientRepository.existsByEmail(client.getEmail())) {
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "Email already in use").toString());
		}
		Optional<Company> company = companyRepository.findById(id);	
		if (company.isPresent()) {
			Company emp = new Company();
			List<Company> companies = new ArrayList<>();
			companies.add(emp.optionalToCompany(company));
			client.setCompanies(companies);
			clientRepository.save(client);
			for (Phone tel : client.getPhones()) {
				tel.setClient(client);
				phoneRepository.save(tel);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(client);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	// cadastro de novo representative
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/{id}/new-representative")
	public ResponseEntity<?> saveRepresentative(@Valid @RequestBody Representative representative,
			@PathVariable Long id, BindingResult bindingResult, MethodArgumentNotValidException m ) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		}
		try {
			validateRepresentative(representative);
			Optional<Company> company = companyRepository.findById(id);
			Company emp = new Company();

			if (company.isPresent()) {
				List<Company> companies = new ArrayList<>();
				companies.add(emp.optionalToCompany(company));
				representative.setCompanies(companies);
				representative.setPassword(bCryptEncoder.encode(representative.getPassword()));
				Profile profile = Profile.REPRESENTATIVE;
				representative.addProfile(profile);
				representativeRepository.save(representative);
				for (Phone tel : representative.getPhones()) {
					tel.setRepresentative(representative);
					phoneRepository.save(tel);
				}

				RepresentativeDTO representativeDTO = new RepresentativeDTO();
				RepresentativeDTO dtoWanted = representativeDTO.toRepresentativeDTO(representative);
				return ResponseEntity.status(HttpStatus.CREATED).body(dtoWanted);

			} else
				return ResponseEntity.badRequest().build();

		} catch (ConstraintViolationException e) {
			
			JSONObject jsonObject = null;
			JSONArray jsonArray = new JSONArray();
			jsonObject = new JSONObject().put("Field", e.getCause());
			jsonObject.put("Message", e.getMessage());
			jsonArray.put(jsonObject);
			JSONObject json = new JSONObject().put("Errors", jsonArray);
		
			return ResponseEntity.badRequest().body(json.toString());
		}
	}

	// associar representative já cadastrado
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PatchMapping("/{id_company}/representatives/{id_representative}")
	public ResponseEntity<?> addRepresentative(@PathVariable Long id_company, @PathVariable Long id_representative) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

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
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PatchMapping("/{id_company}/disassociate-representative/{id_representative}")
	public ResponseEntity<?> removeRepresentative(@PathVariable Long id_company, @PathVariable Long id_representative) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

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
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		if (companyRepository.existsById(id)) {
			companyRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// atualização parcial da company
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialCompanyUpdate(@PathVariable Long id, @RequestBody Company bodyCompany)
			throws IllegalAccessException {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Company baseCompany = new Company();
		baseCompany = baseCompany.optionalToCompany(companyRepository.findById(id));
		PartialUpdateValidation validation = new PartialUpdateValidation();

		bodyCompany = (Company) validation.updateFields(bodyCompany, baseCompany);

		bodyCompany.setId(id);
		companyRepository.save(bodyCompany);
		dto = dto.toCompanyDTO(bodyCompany);
		return ResponseEntity.ok(dto);
	}

	// atualização completa
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> fullCompanyUpdate(@RequestBody Company company, @PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
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
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PatchMapping("{id}/logo")
	public ResponseEntity<?> updateLogoCompany(@RequestBody FileUpload file, @PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		ImageValidations imageValidations = new ImageValidations();
		if (!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());

		} else if (companyRepository.existsById(id)) {
			Optional<Company> companyOptional = companyRepository.findById(id);

			Company company = new Company();
			company = company.optionalToCompany(companyOptional);
			if (!company.getLogo().isEmpty()) {
				String[] fileName = companyOptional.get().getLogo().split("/");
				firebase.delete(fileName[4]);
			}

			// garantindo nome único, função será separada depois
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() + file.getFileName();

			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));

			company = company.optionalToCompany(companyOptional);
			company.setLogo(url.getUrl());
			companyRepository.save(company);
			return ResponseEntity.ok().body(company.getLogo());
		}
		return ResponseEntity.badRequest().build();
	}

	/* #############delete do logo da empresa########### */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("{id}/logo")
	public ResponseEntity<?> deleteLogo(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
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
