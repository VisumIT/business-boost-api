package com.visumIT.Business.boost.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;

public class RepresentativeWithoutCompaniesDTO {
	
	private Long id;		
	
	private List<Phone> phones = new  ArrayList<>();
	
	private String cpf;
	
	private String email;
	
	private String description;
	
	private String photograph;
	
	private String name;
	
	private Date dateOfBirth;

	public RepresentativeWithoutCompaniesDTO toRepresentativeDTO( Representative representative) {	
		RepresentativeWithoutCompaniesDTO dto = new RepresentativeWithoutCompaniesDTO();
		dto.setName(representative.getName());
		dto.setId(representative.getId());
		dto.setCpf(representative.getCpf());
		dto.setDescription(representative.getDescription());
		dto.setEmail(representative.getEmail());
		dto.setPhones(representative.getPhones());	
		dto.setPhotograph(representative.getPhotograph());
		dto.setDateOfBirth(representative.getDateOfBirth());
		return dto;
	}
	
	public List<RepresentativeWithoutCompaniesDTO> toRepresentativesDTO(List<Representative> representatives) {
		//criando o array de empresas dto 
		RepresentativeWithoutCompaniesDTO dto = new RepresentativeWithoutCompaniesDTO();
		List<RepresentativeWithoutCompaniesDTO> dtos = new ArrayList<>();
		for(Representative representative: representatives) {			
			dtos.add(dto.toRepresentativeDTO(representative));
		}
		return dtos;
	}
	
    public RepresentativeWithoutCompaniesDTO optionalToRepresentativeDTO(Optional<Representative> optional){
    	RepresentativeWithoutCompaniesDTO dto = new RepresentativeWithoutCompaniesDTO();
    	    	
    	dto.setId(optional.get().getId());
    	dto.setEmail(optional.get().getEmail());
    	dto.setPhotograph(optional.get().getPhotograph());
    	dto.setName(optional.get().getName());
    	dto.setPhones(optional.get().getPhones());
    	dto.setCpf(optional.get().getCpf());
    	dto.setDescription(optional.get().getDescription());
    	dto.setDateOfBirth(optional.get().getDateOfBirth());
    	
    	return dto;
    }
	/*getters e setters*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
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
}
