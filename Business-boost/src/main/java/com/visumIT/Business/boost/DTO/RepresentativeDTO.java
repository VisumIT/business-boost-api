/*
* Author: Kaique
* classe para tratar o retorno do representante sem comprometer informações sensiveis
* */

package com.visumIT.Business.boost.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.models.Phone;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RepresentativeDTO {
	
	private Long id;		
	
	private List<Phone> phones = new  ArrayList<>();
	
	private String cpf;
	
	private String email;
	
	private String description;
	
	private String photograph;
	
	private String name;
	
	private Date dateOfBirth;
	
	private List<CompanyDTO> empresas = new ArrayList<>();
	

	
	public RepresentativeDTO toRepresentativeDTO( Representative representative) {	
		RepresentativeDTO dto = new RepresentativeDTO();
		CompanyDTO companyDTO = new CompanyDTO();
		dto.setName(representative.getName());
		dto.setId(representative.getId());
		dto.setCpf(representative.getCpf());
		dto.setDescription(representative.getDescription());
		dto.setEmail(representative.getEmail());
		dto.setPhones(representative.getPhones());	
		dto.setPhotograph(representative.getPhotograph());
		dto.setDateOfBirth(representative.getDateOfBirth());
		
		dto.setCompanies(companyDTO.toCompaniesDTO(representative.getCompanies()));
		return dto;
	}


	public List<RepresentativeDTO> toRepresentativesDTO(List<Representative> representatives) {
		//criando o array de empresas dto 
		RepresentativeDTO dto = new RepresentativeDTO();
		List<RepresentativeDTO> representantesDTO = new ArrayList<>();
		for(Representative representative: representatives) {			
			representantesDTO.add(dto.toRepresentativeDTO(representative));
		}
		return representantesDTO;
	}	
	
    public RepresentativeDTO optionalToRepresentativeDTO(Optional<Representative> optional){
    	RepresentativeDTO dto = new RepresentativeDTO();
    	CompanyDTO dtoCompany = new CompanyDTO();
    	
    	dto.setId(optional.get().getId());
    	dto.setEmail(optional.get().getEmail());
    	dto.setCompanies(dtoCompany.toCompaniesDTO(optional.get().getCompanies()));
    	dto.setPhotograph(optional.get().getPhotograph());
    	dto.setName(optional.get().getName());
    	dto.setPhones(optional.get().getPhones());
    	dto.setCpf(optional.get().getCpf());
    	dto.setDescription(optional.get().getDescription());
    	dto.setDateOfBirth(optional.get().getDateOfBirth());
    	
    	return dto;
    }
	
	
	//Construtor
	public RepresentativeDTO() {}
	//******************************************GETTERS e SETTERS***********************/

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public List<Phone> getPhones() {
		return phones;
	}


	public void setPhones(List<Phone> phone) {
		this.phones = phone;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getPhotograph() {
		return photograph;
	}


	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public List<CompanyDTO> getCompanies() {
		return empresas;
	}


	public void setCompanies(List<CompanyDTO> empresas) {
		this.empresas = empresas;
	}



	

}
