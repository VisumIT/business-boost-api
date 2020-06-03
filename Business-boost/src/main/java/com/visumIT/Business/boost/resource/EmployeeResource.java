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
@RequestMapping("/empresas/{id}/employees")
public class EmployeeResource {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    //retorna os employees de uma empresa
    @GetMapping
    public ResponseEntity<?> getEmployees (@PathVariable Long id){
        Optional<Company> empresaProcurada = companyRepository.findById(id);

        if(empresaProcurada.isPresent()){
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
    public ResponseEntity<?> getEmployee(@PathVariable(name="id") Long id_empresa, @PathVariable(name="id_employee") Long id_employee){
    	Optional<Employee> employeeProcurado = employeeRepository.findById(id_employee);
    	Optional<Company> empresaProcurada = companyRepository.findById(id_empresa);
    	
    	if(empresaProcurada.isPresent()&& employeeProcurado.isPresent()) {
    		EmployeeDTO dto = new EmployeeDTO();
    		return ResponseEntity.ok(dto.optionalToEmployeeDTO(employeeProcurado));
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    	
    }
    //cadastro de um novo employee
    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody Employee employee,@PathVariable(name = "id") Long id_empresa,
    		BindingResult bindingResult){
    	
    	Optional<Company> empresaProcurada = companyRepository.findById(id_empresa);

        if(!empresaProcurada.isPresent()){
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
            Optional<Company> empresaOptional = companyRepository.findById(id_empresa);

            if(empresaOptional.isPresent()){
                Company empresa = new Company();
                employee.setCompany(empresa.optionalToCompany(empresaOptional));

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
    
    /*alterar um employee*/
    @PutMapping("/{id_employee}")
    public ResponseEntity<?> atualizarEmployee(@Valid @RequestBody Employee employee, @PathVariable(name="id") Long id_empresa ,
    		@PathVariable(name="id_employee") Long id_employee, BindingResult bindingResult){
    	
    	Optional<Company> empresaProcurada = companyRepository.findById(id_empresa);
    	Optional<Employee> employeeProcurado = employeeRepository.findById(id_employee);
    	
    	if(empresaProcurada.isPresent() && employeeProcurado.isPresent()){
    		Company emp = new Company();
        	employee.setId(id_employee);
			int i = 0;
			// seta os ids do telefone para poder atualizar
			for (Phone tel : employee.getPhones()) {
				tel.setEmployee(employee);
				tel.setId(employeeProcurado.get().getPhones().get(i).getId());
				phoneRepository.save(tel);
				i++;
			}
			employee.setCompany(emp.optionalToCompany(empresaProcurada));
			
			if(employee.getPassword()==null) {
				employee.setPassword(employeeProcurado.get().getPassword());
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
