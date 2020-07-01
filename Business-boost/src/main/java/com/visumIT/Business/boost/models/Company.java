/*
*Author: Kaique
*
*classe representa o modelo da entidade Company
*/
package com.visumIT.Business.boost.models;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visumIT.Business.boost.enums.Profile;

@Entity
@Table(name = "tbl_companies")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tbl_profiles")
	private Set<Integer> profiles = new HashSet<>();

	@OneToMany(mappedBy = "company")
	private List<Order> orders = new ArrayList<>();

	@OneToMany(mappedBy = "company")
	private List<Product> products = new ArrayList<>();

	// Representative
	@ManyToMany(mappedBy = "companies")
	@JsonIgnore
	private List<Representative> representatives = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	@Column(name = "phones", columnDefinition = "VARCHAR(20)")
	private List<Phone> phones = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	private List<Brand> brand = new ArrayList<>();

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

	@Size(max = 50)
	@Column(name = "city", columnDefinition = "VARCHAR(50)")
	private String city;

	@Size(min = 8)
	@Column(name = "cep", columnDefinition = "VARCHAR(20)")
	private String cep;
	// ********************

	@Size(max = 100)
	@Column(name = "site", columnDefinition = "VARCHAR(100)")
	private String site;

	@Size(min = 14, max = 20)
	@NotBlank
	@Column(name = "state_registration", columnDefinition = "VARCHAR(20)")
	private String stateRegistration;

	@Size(max = 60)
	@NotBlank
	@Column(name = "company_name", columnDefinition = "VARCHAR(60)")
	private String companyName;

	@NotBlank(message = "{cnpj.not.blank}")
	@CNPJ(message = "{cnpj.Company.cnpj}")
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)", unique = true)
	private String cnpj;

	@Size(max = 40)
	@Column(name = "fictitious_name", columnDefinition = "VARCHAR(40)")
	private String fictitiousName;

	@NotBlank(message = "{email.not.blank}")
	@Email(message = "{email.not.valid}")
	@Column(name = "email", columnDefinition = "VARCHAR(40)", unique = true)
	private String email;

	// @JsonIgnore
	@NotBlank(message = "{Password.not.blank}")
	@Size(min = 8, message = "{Size.Company.Password}")
	@Column(name = "password", columnDefinition = "VARCHAR(255)")
	private String password;

	@Column(name = "description", columnDefinition = "VARCHAR(200)")
	private String description;

	@Column(name = "logo", columnDefinition = "VARCHAR(255)")
	private String logo;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	@Column(name = "employees")
	private List<Employee> employees = new ArrayList<>();

	public Company() {
		this.addProfile(Profile.ADMIN);
	}

	// transformar um tipo opcional no tipo Company
	public Company optionalToCompany(Optional<Company> optional) {
		Company Company = new Company();

		Company.setId(optional.get().getId());
		Company.setNeighborhood(optional.get().getNeighborhood());
		Company.setCep(optional.get().getCep());
		Company.setCity(optional.get().getCity());
		Company.setCnpj(optional.get().getCnpj());
		Company.setDescription(optional.get().getDescription());
		Company.setEmail(optional.get().getEmail());
		Company.setAddress(optional.get().getAddress());
		Company.setStateRegistration(optional.get().getStateRegistration());
		Company.setLogo(optional.get().getLogo());
		Company.setPublicPlace(optional.get().getPublicPlace());
		Company.setFictitiousName(optional.get().getFictitiousName());
		Company.setNumber(optional.get().getNumber());
		Company.setCompanyName(optional.get().getCompanyName());
		Company.setRepresentatives(optional.get().getRepresentatives());
		Company.setPassword(optional.get().getPassword());
		Company.setSite(optional.get().getSite());
		Company.setPhones(optional.get().getPhones());
		Company.setUf(optional.get().getUf());

		return Company;
	}

	/* ######################## GETTERS AND SETTERS############################# */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Representative> getRepresentatives() {
		return representatives;
	}

	public void setRepresentatives(List<Representative> representatives) {
		this.representatives = representatives;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public List<Brand> getBrand() {
		return brand;
	}

	public void setBrand(List<Brand> brand) {
		this.brand = brand;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Set<Profile> getProfiles() {
		return profiles.stream().map(x -> Profile.toEnum(x)).collect(Collectors.toSet());
	}

	public void addProfile(Profile profile) {
		profiles.add(profile.getId());
	}

	@Override
	public String toString() {
		return this.companyName;
	}

	public List<Product> getProduct() {
		return products;
	}

	public void setProduct(List<Product> products) {
		this.products = products;
	}
}
