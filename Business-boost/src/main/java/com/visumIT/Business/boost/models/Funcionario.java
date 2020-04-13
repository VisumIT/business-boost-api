package com.visumIT.Business.boost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Funcionario")
@Table(name="tbl_funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "empresa")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Empresa empresa;

    @Column(name="matricula", columnDefinition = "VARCHAR(30)")
    private String matricula;

    @Column(name = "nome", columnDefinition = "VARCHAR(40)")
    private String nome;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "funcionario")
    @Column(name = "telefone", columnDefinition = "VARCHAR(20)")
    private List<Telefone> telefone = new ArrayList<>();

    @Email
    @Column(name = "email", columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(name = "senha", columnDefinition = "VARCHAR(150)")
    private String senha;

    @Column(name="foto", columnDefinition = "VARCHAR(45)")
    private String foto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Telefone> getTelefone() {
        return telefone;
    }

    public void setTelefone(List<Telefone> telefone) {
        this.telefone = telefone;
    }
}
