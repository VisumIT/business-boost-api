/*author: kaique*/
package com.visumIT.Business.boost.models;

import java.util.ArrayList;
import java.util.List;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_customers")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Address
	@Size(max = 150)
	@Column(name = "address", columnDefinition = "VARCHAR(150)")
	private String address;

	@Size(min = 3, max = 20)
	@Column(name = "public_place", columnDefinition = "VARCHAR(20)")
	private String publicPlace;

	@Size(max = 10)
	@Column(name = "number", columnDefinition = "VARCHAR(10)")
	private String number;

	@Size(min = 2)
	@Column(name = "uf", columnDefinition = "VARCHAR(20)")
	private String uf;

	@Size(max = 50)
	@Column(name = "neighborhood", columnDefinition = "VARCHAR(50)")
	private String neighborhood;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tbl_company_client", joinColumns = @JoinColumn
													(name = "client_id", referencedColumnName = "id"),
													inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
	private List <Company> companies = new ArrayList<>();

	@Size(max = 50)
	@Column(name = "city", columnDefinition = "VARCHAR(50)")
	private String city;

	@Size(min = 8)
	@Column(name = "cep", columnDefinition = "VARCHAR(20)")
	private String cep;
	// ********************
	@NotBlank
	@Column(name = "name", columnDefinition = "VARCHAR(255)")
	private String name;
	
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)", unique=true)
	private String cnpj;
	
	@NotBlank(message="{email.not.blank}")
	@Email(message="{email.not.valid}")
	@Column(name = "email", columnDefinition = "VARCHAR(40)", unique=true)
	private String email;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "client")
	@Column(name = "phones", columnDefinition = "VARCHAR(20)")
	private List<Phone> phones = new  ArrayList<>();

	
	/*GETTERS & SETTERS*/
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	
}
