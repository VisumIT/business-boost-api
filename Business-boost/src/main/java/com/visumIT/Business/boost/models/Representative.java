package com.visumIT.Business.boost.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.Data;

/* Author: kaique
 * Classe para representar a tabela de representative do banco
*
 * */


@Entity(name="Representative")
@Table(name="tbl_representative")
public class Representative {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//company
	@ManyToMany
	@JoinTable(name = "tbl_company_representative", joinColumns = @JoinColumn
													(name = "representative_id", referencedColumnName = "id"),
													inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
	private List <Company> companys = new ArrayList<>();
	
	
	@Column(name="name", columnDefinition = "VARCHAR(40)")
	private String name;
	
	@Email
	@Column(name="email",columnDefinition = "VARCHAR(40)")
	private String email;
	
	@Column(name="password", columnDefinition = "VARCHAR(150)")
	private String password;
	
	@Column(name="photograph", columnDefinition = "VARCHAR(40)")
	private String photograph;
	
	//relação
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "representative")
	@Column(name = "phone", columnDefinition = "VARCHAR(20)")
	private List<Phone> phone = new  ArrayList<>();
	
	@Column(name="cpf", columnDefinition="VARCHAR(20)")
	private String cpf;
	
	@Column(name="data_nascimento")
	private Date dateOfBirth;
	
	@Column(name="gender", columnDefinition="VARCHAR(1)")
	private String gender;
	
	@Column(name="description")
	private String description;
	
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
		return companys;
	}

	public void setCompanies(List<Company> companys) {
		this.companys = companys;
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
		return phone;
	}

	public void setPhones(List<Phone> phone) {
		this.phone = phone;
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
	
	
	
}
