package com.visumIT.Business.boost.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.models.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long id;

    @JsonIgnore
    private Company company;

    private String registration;

    private String name;

    private List<Phone> phones = new ArrayList<>();

    private String email;

    private String photograph;

    //converte objeto do tipo funcionario para funcionarioDTO
    public EmployeeDTO toEmployeeDTO (Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        
        dto.setEmail(employee.getEmail());
        dto.setCompany(employee.getCompany());
        dto.setPhotograph(employee.getPhotograph());
        dto.setId(employee.getId());
        dto.setRegistration(employee.getRegistration());
        dto.setName(employee.getName());
        dto.setPhones(employee.getPhones());

        return dto;
    }

    public List<EmployeeDTO> toEmployeesDTO(List<Employee> employees){
        EmployeeDTO dto = new EmployeeDTO();
        List<EmployeeDTO> funcionariosDTO = new ArrayList<>();
        for(Employee employee: employees){
            funcionariosDTO.add(dto.toEmployeeDTO(employee));
        }
        return funcionariosDTO;
    }
    
    public EmployeeDTO optionalToEmployeeDTO(Optional<Employee> optional){
    	EmployeeDTO dto = new EmployeeDTO();
    	
    	dto.setId(optional.get().getId());
    	dto.setEmail(optional.get().getEmail());
    	dto.setCompany(optional.get().getCompany());
    	dto.setPhotograph(optional.get().getPhotograph());
    	dto.setRegistration(optional.get().getRegistration());
    	dto.setName(optional.get().getName());
    	dto.setPhones(optional.get().getPhones());
    	
    	return dto;
    }

    /*#####################GETTERS and SETTERS*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotograph() {
		return photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}
   
}
