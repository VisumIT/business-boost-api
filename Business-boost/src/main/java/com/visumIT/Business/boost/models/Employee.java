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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visumIT.Business.boost.enums.Profile;

@Entity
@Table(name="tbl_employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "company")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Company company;
    
    
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="tbl_profiles")
    private Set<Integer> profiles = new HashSet<>();

    @Column(name="registration", columnDefinition = "VARCHAR(30)")
    private String registration;

    @Column(name = "name", columnDefinition = "VARCHAR(40)")
    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "employee")
    @Column(name = "phones", columnDefinition = "VARCHAR(20)")
    private List<Phone> phones = new ArrayList<>();

    @Email
    @Column(name = "email", columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(150)")
    private String password;

    @Column(name="photograph")
    private String photograph;

    
    public Employee optionalToEmployee(Optional<Employee> optionalEmployee){
    	Employee employee = new Employee();
    	
    	employee.setCompany(optionalEmployee.get().getCompany());
    	employee.setEmail(optionalEmployee.get().getEmail());
    	employee.setId(optionalEmployee.get().getId());
    	employee.setName(optionalEmployee.get().getName());
    	employee.setPassword(optionalEmployee.get().getPassword());
    	employee.setPhones(optionalEmployee.get().getPhones());
    	employee.setPhotograph(optionalEmployee.get().getPhotograph());
    	employee.setRegistration(optionalEmployee.get().getRegistration());
    	
    	return employee;
    }
    
    
    
/*#############################Getters and Setters*/
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

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
    
    public Set<Profile> getProfiles() {
    	return profiles.stream().map(x -> Profile.toEnum(x)).collect(Collectors.toSet());
    }
    
    public void addProfile(Profile profile) {
    	profiles.add(profile.getId());
    }
}
