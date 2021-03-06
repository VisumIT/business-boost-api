package com.visumIT.Business.boost.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import com.visumIT.Business.boost.enums.Profile;

/* Author: kaique
 * Classe para representar a tabela de representative do banco
*
 * */


@Entity
@Table(name="tbl_representatives")
public class Representative {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="tbl_profiles")
    private Set<Integer> profiles = new HashSet<>();
	
	//company
	@ManyToMany
	@JoinTable(name = "tbl_company_representative", joinColumns = @JoinColumn
													(name = "representative_id", referencedColumnName = "id"),
													inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
	private List <Company> companies = new ArrayList<>();
	
	
	@Column(name="name", columnDefinition = "VARCHAR(40)")
	private String name;
	
	@Email
	@Column(name="email",columnDefinition = "VARCHAR(40)", unique=true)
	private String email;
	
	@NotBlank
	@Column(name="password", columnDefinition = "VARCHAR(150)")
	private String password;
	
	@Column(name="photograph")
	private String photograph;
	
	//relação
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "representative")
	@Column(name = "phones", columnDefinition = "VARCHAR(20)")
	private List<Phone> phones = new  ArrayList<>();
	
	@NotBlank
	@CPF(message="{cpf.Representative.cpf}")
	@Column(name="cpf", columnDefinition="VARCHAR(20)", unique=true)
	private String cpf;
	
	@Column(name="date_birth")
	private Date dateOfBirth;
	
	@Column(name="gender", columnDefinition="VARCHAR(1)")
	private String gender;
	
	@Column(name="description")
	private String description;
	
	
	
	public Representative() {
		this.addProfile(Profile.REPRESENTATIVE);
	}

	public Representative optionalToRepresentative( Optional<Representative> optional) {
		Representative rep = new Representative();
		
		rep.setId(optional.get().getId());
		rep.setCpf(optional.get().getCpf());
		rep.setDescription(optional.get().getDescription());
		rep.setEmail(optional.get().getEmail());
		rep.setPhotograph(optional.get().getPhotograph());
		rep.setCompanies(optional.get().getCompanies());
		rep.setPassword(optional.get().getPassword());
		rep.setPhones(optional.get().getPhones());
		rep.setName(optional.get().getName());
		
		return rep;
}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhotograph() {
		return photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
	public Set<Profile> getProfiles() {
    	return profiles.stream().map(x -> Profile.toEnum(x)).collect(Collectors.toSet());
    }
    
    public void addProfile(Profile profile) {
    	profiles.add(profile.getId());
    }
	
	
}
