package com.visumIT.Business.boost.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visumIT.Business.boost.enums.Profile;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;

public class CompanyWithoutEmployeesDTO {
	private Long id;

	// representantes
	@JsonIgnore
	private List<Representative> representatives;

	// address
	private String address;

	private String publicPlace;

	private String number;

	private String uf;

	private String neighborhood;

	private String city;

	private String cep;
	// ********************

	private String site;

	private List<Phone> phones = new ArrayList<>();

	//private List<Employee> employees = new ArrayList<>();

	private String stateRegistration;

	private String companyName;

	private String cnpj;

	private String fictitiousName;

	private String email;

	private String description;

	private String logo;
	
	private Set<Profile> profiles = new HashSet<>();

	public CompanyWithoutEmployeesDTO toCompanyDTO( Company company) {
		CompanyWithoutEmployeesDTO dto = new CompanyWithoutEmployeesDTO();
		dto.setId(company.getId());
		dto.setNeighborhood(company.getNeighborhood());
		dto.setCep(company.getCep());
		dto.setCity(company.getCity());
		dto.setCnpj(company.getCnpj());
		dto.setDescription(company.getDescription());
		dto.setEmail(company.getEmail());
		dto.setAddress(company.getAddress());
		dto.setStateRegistration(company.getStateRegistration());
		dto.setLogo(company.getLogo());
		dto.setPublicPlace(company.getPublicPlace());
		dto.setFictitiousName(company.getFictitiousName());
		dto.setNumber(company.getNumber());
		dto.setCompanyName(company.getCompanyName());
		dto.setSite(company.getSite());
		dto.setPhones(company.getPhones());
		dto.setUf(company.getUf());
		dto.setProfiles(company.getProfiles());
		
		dto.setRepresentatives(company.getRepresentatives());
		return dto;
	}
	
	public List<CompanyWithoutEmployeesDTO> toCompaniesDTO(List<Company> companies) {
		//criando o array de companies dto 
		CompanyWithoutEmployeesDTO dto = new CompanyWithoutEmployeesDTO();
		List<CompanyWithoutEmployeesDTO> companiesDTO = new ArrayList<>();
		for(Company company: companies) {			
			companiesDTO.add(dto.toCompanyDTO(company));
		}
		return companiesDTO;
	}
	
	/*getters e setters*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Representative> getRepresentatives() {
		return representatives;
	}

	public void setRepresentatives(List<Representative> representatives) {
		this.representatives = representatives;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPublicPlace() {
		return publicPlace;
	}

	public void setPublicPlace(String publicPlace) {
		this.publicPlace = publicPlace;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public String getStateRegistration() {
		return stateRegistration;
	}

	public void setStateRegistration(String stateRegistration) {
		this.stateRegistration = stateRegistration;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getFictitiousName() {
		return fictitiousName;
	}

	public void setFictitiousName(String fictitiousName) {
		this.fictitiousName = fictitiousName;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
    public Set<Profile> getProfiles() {
    	return profiles;
    }
    
	public void addProfile(Profile profile) {
    	profiles.add(profile);
    }
	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}
	
}
