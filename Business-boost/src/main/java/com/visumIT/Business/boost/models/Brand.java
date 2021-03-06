/*Autor: kaique
* */

package com.visumIT.Business.boost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Optional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="tbl_brands")
public class Brand {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min=2, max=30)
    @Column(name="name", columnDefinition = "VARCHAR(30)")
    private String name;

    @JoinColumn(name = "company")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Company company;

    @Column
    private String logo;
    
    @Column(columnDefinition = "VARCHAR(200)")
    private String description;

    
    
    
    
    public Brand optionalToBrand(Optional<Brand> optionalBrand) {
    	Brand brand = new Brand();
    	
    	brand.setCompany(optionalBrand.get().getCompany());
    	brand.setDescription(optionalBrand.get().getDescription());
    	brand.setId(optionalBrand.get().getId());
    	brand.setLogo(optionalBrand.get().getLogo());
    	brand.setName(optionalBrand.get().getName());    	
    	
    	return brand;
    }
    
    /*############# Getters and Setters ######################*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
}
