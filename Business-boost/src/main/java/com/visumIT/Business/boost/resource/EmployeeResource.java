/*
 * Author: Kaique
 * */
package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.DTO.EmployeeDTO;
import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.EmployeeRepository;
import com.visumIT.Business.boost.repository.PhoneRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies/{id}/employees")
public class EmployeeResource {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PhoneRepository phoneRepository;
    //valida update de funcionarios
    private Employee validUpdate(Employee bodyEmployee, Long id) {
		Employee baseEmployee =  bodyEmployee.optionalToEmployee(employeeRepository.findById(id));
		bodyEmployee.setId(id);
		if(bodyEmployee.getCompany()==null) {
			bodyEmployee.setCompany(baseEmployee.getCompany());
		}
		if(bodyEmployee.getEmail()==null) {
			bodyEmployee.setEmail(baseEmployee.getEmail());
		}		
		if(bodyEmployee.getName()==null) {
			bodyEmployee.setName(baseEmployee.getName());
		}		
		if(bodyEmployee.getPassword()==null) {
			bodyEmployee.setPassword(baseEmployee.getPassword());
		}		
		if(bodyEmployee.getPhones()==null) {
			bodyEmployee.setPhones(baseEmployee.getPhones());
		}	
		if(bodyEmployee.getPhotograph()==null) {
			bodyEmployee.setPhotograph(baseEmployee.getPhotograph());
		}		
		if(bodyEmployee.getRegistration()==null) {
			bodyEmployee.setRegistration(baseEmployee.getRegistration());
		}
		
		return bodyEmployee;
	} 
    
    //retorna os employees de uma company
    @GetMapping
    public ResponseEntity<?> getEmployees (@PathVariable Long id){
        Optional<Company> companyWanted = companyRepository.findById(id);

        if(companyWanted.isPresent()){
            List<Employee> employees = employeeRepository.findAll();
            EmployeeDTO employeeDTO = new EmployeeDTO(); 
            //protegendo a senha dos usuarios
            List<EmployeeDTO> employeesDTO = employeeDTO.toEmployeesDTO(employees);
            return ResponseEntity.ok().body(employeesDTO);
        }else
            return ResponseEntity.notFound().build();
    }
    
    /*Retorna um employee especifico*/
    @GetMapping("{id_employee}")
    public ResponseEntity<?> getEmployee(@PathVariable(name="id") Long id_company, @PathVariable(name="id_employee") Long id_employee){
    	Optional<Employee> employeeWanted = employeeRepository.findById(id_employee);
    	Optional<Company> companyWanted = companyRepository.findById(id_company);
    	
    	if(companyWanted.isPresent()&& employeeWanted.isPresent()) {
    		EmployeeDTO dto = new EmployeeDTO();
    		return ResponseEntity.ok(dto.optionalToEmployeeDTO(employeeWanted));
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    	
    }
    //cadastro de um novo employee
    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee,@PathVariable(name = "id") Long id_company,
    		BindingResult bindingResult){
    	
    	Optional<Company> companyWanted = companyRepository.findById(id_company);

        if(!companyWanted.isPresent()){
        	return ResponseEntity.notFound().build();
        }
        if(employeeRepository.existsByEmail(employee.getEmail())){
            return ResponseEntity.badRequest().body(new JSONObject()
                    .put("message", "E-mail allready in use").toString());
            
        }else if(employeeRepository.existsByRegistration(employee.getRegistration())){
            return ResponseEntity.badRequest().body(new JSONObject()
                    .put("message", "Registration allready in use")
                    .toString());
            
        }else if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
            
        }else{
            employeeRepository.save(employee);
            Optional<Company> companyOptional = companyRepository.findById(id_company);

            if(companyOptional.isPresent()){
                Company company = new Company();
                employee.setCompany(company.optionalToCompany(companyOptional));

                for (Phone tel : employee.getPhones()) {
                    tel.setEmployee(employee);
                    phoneRepository.save(tel);
                }
                employeeRepository.save(employee);
                return ResponseEntity.status(HttpStatus.CREATED).body(employee);
            }else
                return ResponseEntity.badRequest().build();
        }
    }
    
    //atualização parcial
    @PatchMapping("/{id_employee}")
    public ResponseEntity<?> partialUpdate(@Valid @RequestBody Employee employee, @PathVariable(name="id_employee") Long id_employee,
    		@PathVariable(name="id")Long id_company){
    	Optional<Company> companyWanted = companyRepository.findById(id_company);
    	Optional<Employee> employeeWanted = employeeRepository.findById(id_employee);
    	Company company = new Company();
    	company = company.optionalToCompany(companyWanted);
    	
    	if(companyWanted.isPresent() && employeeWanted.isPresent() && employeeRepository.existsByCompany(company)) {
    		employee = validUpdate(employee, id_employee);
    		employeeRepository.save(employee);
    		EmployeeDTO dto = new EmployeeDTO();
    		return ResponseEntity.status(HttpStatus.CREATED).body(dto.toEmployeeDTO(employee));
    	}else {
    		return ResponseEntity.badRequest().build();
    	}
    }
    
    /*update completo*/
    @PutMapping("/{id_employee}")
    public ResponseEntity<?> fullEmployeeUpdate(@Valid @RequestBody Employee employee, @PathVariable(name="id") Long id_company ,
    		@PathVariable(name="id_employee") Long id_employee, BindingResult bindingResult){
    	
    	Optional<Company> companyWanted = companyRepository.findById(id_company);
    	Optional<Employee> employeeWanted = employeeRepository.findById(id_employee);
    	
    	if(companyWanted.isPresent() && employeeWanted.isPresent()){
    		Company emp = new Company();
        	employee.setId(id_employee);
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Phone tel : employee.getPhones()) {
				tel.setEmployee(employee);
				tel.setId(employeeWanted.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
			}
			employee.setCompany(emp.optionalToCompany(companyWanted));
			
			if(employee.getPassword()==null) {
				employee.setPassword(employeeWanted.get().getPassword());
			}
			employeeRepository.save(employee);
			return ResponseEntity.status(HttpStatus.OK).body(employee);
    
        }else if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
            }
        else {
        	return ResponseEntity.notFound().build();
        }	
    }
       
    /*deletar employee*/
    @DeleteMapping("/{id_employee}")
    public ResponseEntity<?> deletarEmployee(@PathVariable Long id_employee){
    	if(employeeRepository.existsById(id_employee)) {
    		employeeRepository.deleteById(id_employee);
    		return ResponseEntity.noContent().build();
    	}
    	else {
    		return ResponseEntity.notFound().build();
    	}
    }
}
