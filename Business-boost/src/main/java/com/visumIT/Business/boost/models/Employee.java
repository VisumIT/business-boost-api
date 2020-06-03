package com.visumIT.Business.boost.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="Employee")
@Table(name="tbl_employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "company")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Company company;

    @Column(name="registration", columnDefinition = "VARCHAR(30)")
    private String registration;

    @Column(name = "name", columnDefinition = "VARCHAR(40)")
    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "employee")
    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private List<Phone> phone = new ArrayList<>();

    @Email
    @Column(name = "email", columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(150)")
    private String password;

    @Size(min=0, max=40, message="{Size.employee.photograph}")
    @Column(name="photograph", columnDefinition = "VARCHAR(45)")
    private String photograph;


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
        return phone;
    }

    public void setPhones(List<Phone> phone) {
        this.phone = phone;
    }
}
