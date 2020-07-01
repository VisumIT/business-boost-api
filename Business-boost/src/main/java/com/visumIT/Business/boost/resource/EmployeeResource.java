/*
 * Author: Kaique
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
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.DTO.EmployeeDTO;
import com.visumIT.Business.boost.enums.Profile;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.functions.ImageValidations;
import com.visumIT.Business.boost.functions.PartialUpdateValidation;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.EmployeeRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;
import com.visumIT.Business.boost.security.UserSS;
import com.visumIT.Business.boost.services.UserService;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;
import com.visumIT.Business.boost.upload.FileUpload;
import com.visumIT.Business.boost.upload.FileUploadUrl;
import com.visumIT.Business.boost.upload.FirebaseStorageService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies/{id}/employees")
public class EmployeeResource {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;
	
	@Autowired
	private FirebaseStorageService firebase;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private PhoneRepository phoneRepository;

	private String validEmployee(Long id, Employee employee) {
		Company company = new Company();
		Optional<Company> companyWanted = companyRepository.findById(id);
		company = company.optionalToCompany(companyWanted);
		if (employeeRepository.existsByEmail(employee.getEmail())) {
			return "E-mail allready in use";

		} else if (employeeRepository.existsByRegistration(employee.getRegistration())) {
			return "Registration allready in use";
		}
		return "ok";
	}

	// retorna os employees de uma company
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getEmployees(@PathVariable Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Optional<Company> companyWanted = companyRepository.findById(id);

		if (companyWanted.isPresent()) {
			List<Employee> employees = employeeRepository.findAll();
			EmployeeDTO employeeDTO = new EmployeeDTO();
			// protegendo a senha dos usuarios
			List<EmployeeDTO> employeesDTO = employeeDTO.toEmployeesDTO(employees);
			return ResponseEntity.ok().body(employeesDTO);
		} else
			return ResponseEntity.notFound().build();
	}

	/* Retorna um employee especifico */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("{id_employee}")
	public ResponseEntity<?> getEmployee(@PathVariable(name = "id") Long id_company,
			@PathVariable(name = "id_employee") Long id_employee) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Optional<Employee> employeeWanted = employeeRepository.findById(id_employee);
		Optional<Company> companyWanted = companyRepository.findById(id_company);

		if (companyWanted.isPresent() && employeeWanted.isPresent()) {
			EmployeeDTO dto = new EmployeeDTO();
			return ResponseEntity.ok(dto.optionalToEmployeeDTO(employeeWanted));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	// cadastro de um novo employee
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee,
			@PathVariable(name = "id") Long id_company, BindingResult bindingResult) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String message = validEmployee(id_company, employee);

		if (!message.equals("ok")) {
			if (message.equals("404")) {
				return ResponseEntity.badRequest().build();
			}
			return ResponseEntity.badRequest().body(new JSONObject().put("message", message).toString());
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));

		} else {
			employee.setPassword(bCryptEncoder.encode(employee.getPassword()));
			employeeRepository.save(employee);
			Optional<Company> companyOptional = companyRepository.findById(id_company);

			Company company = new Company();
			employee.setCompany(company.optionalToCompany(companyOptional));

			for (Phone tel : employee.getPhones()) {
				tel.setEmployee(employee);
				phoneRepository.save(tel);
			}
			System.out.println("----------------------");
			System.out.println(employee.getProfiles());
			employeeRepository.save(employee);
			EmployeeDTO dto = new EmployeeDTO();
			return ResponseEntity.status(HttpStatus.CREATED).body(dto.toEmployeeDTO(employee));

		}
	}

	// update parcial
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PatchMapping("/{id_employee}")
	public ResponseEntity<?> partialUpdate(@Valid @RequestBody Employee bodyEmployee,
			@PathVariable(name = "id_employee") Long id_employee, @PathVariable(name = "id") Long id_company) 
					throws IllegalAccessException {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Optional<Company> companyWanted = companyRepository.findById(id_company);
		Company company = new Company();
		company = company.optionalToCompany(companyWanted);

		if (employeeRepository.existsByCompany(company)) {
			PartialUpdateValidation validation = new PartialUpdateValidation();
			Employee baseEmployee = new Employee();
			baseEmployee = baseEmployee.optionalToEmployee(employeeRepository.findById(id_employee));
			bodyEmployee = (Employee)validation.updateFields(bodyEmployee, baseEmployee);
			employeeRepository.save(bodyEmployee);
			EmployeeDTO dto = new EmployeeDTO();
			return ResponseEntity.status(HttpStatus.CREATED).body(dto.toEmployeeDTO(bodyEmployee));
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	/* update completo */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/{id_employee}")
	public ResponseEntity<?> fullEmployeeUpdate(@Valid @RequestBody Employee employee,
			@PathVariable(name = "id") Long id_company, @PathVariable(name = "id_employee") Long id_employee,
			BindingResult bindingResult) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id_company.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		

		Optional<Company> companyWanted = companyRepository.findById(id_company);
		Optional<Employee> employeeWanted = employeeRepository.findById(id_employee);
		Company company = new Company();
		company = company.optionalToCompany(companyWanted);
		if (employeeRepository.existsByCompany(company)) {	
			employee.setId(id_employee);
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Phone tel : employee.getPhones()) {
				tel.setEmployee(employee);
				tel.setId(employeeWanted.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
			}
			employee.setCompany(company);
			employeeRepository.save(employee);
			EmployeeDTO dto = new EmployeeDTO();
			return ResponseEntity.status(HttpStatus.OK).body(dto.toEmployeeDTO(employee));

		} else if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/* deletar employee */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/{id_employee}")
	public ResponseEntity<?> deletarEmployee(@PathVariable Long id_employee, @PathVariable(name="id") Long id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.ADMIN) || !id.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if (employeeRepository.existsById(id_employee)) {
			employeeRepository.deleteById(id_employee);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*upload de foto*/
	@PreAuthorize("hasAnyRole('EMPLOYEE')")
	@PatchMapping("/{id_employee}/photos")
	public ResponseEntity<?>updatePhotograph (@RequestBody FileUpload file,
			@PathVariable(name = "id_employee") Long id_employee, @PathVariable(name = "id") Long id_company) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.EMPLOYEE) || !id_employee.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		
		ImageValidations imageValidations = new ImageValidations();
		Company company = new Company();
		company = company.optionalToCompany(companyRepository.findById(id_company));
		if (!imageValidations.validImage(file)) {
			return ResponseEntity.badRequest()
					.body(new JSONObject().put("message", "please only image files").toString());

		}else if(employeeRepository.existsByCompany(company)) {
			Employee employee = new Employee();
			employee = employee.optionalToEmployee(employeeRepository.findById(id_employee));
			if(!employee.getPhotograph().isEmpty()) {
				String[] fileName = employee.getPhotograph().split("/");
				firebase.delete(fileName[4]);
			}
			//garantindo nome unico
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() + file.getFileName();
			
			FileUploadUrl url = new FileUploadUrl(firebase.upload(file, name));
			
			employee.setPhotograph(url.getUrl());
			employeeRepository.save(employee);
			
			return ResponseEntity.ok().body(employee.getPhotograph());
		}	
		return ResponseEntity.notFound().build();
	}
	
	/*deletar foto*/
	@PreAuthorize("hasAnyRole('EMPLOYEE')")
	@DeleteMapping("{id_employee}/photos")
	public ResponseEntity<?> deleteLogo(@PathVariable(name = "id_employee") Long id_employee, @PathVariable(name = "id") Long id_company){
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Profile.EMPLOYEE) || !id_employee.equals(user.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Company company = new Company();
		company = company.optionalToCompany(companyRepository.findById(id_company));
		if (employeeRepository.existsByCompany(company)) {
			Employee employee = new Employee();
			employee = employee.optionalToEmployee(employeeRepository.findById(id_employee));
			
			String[] fileName = employee.getPhotograph().split("/");

			if (firebase.delete(fileName[4])) {
				employee.setPhotograph(" ");
				employeeRepository.save(employee);

				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
