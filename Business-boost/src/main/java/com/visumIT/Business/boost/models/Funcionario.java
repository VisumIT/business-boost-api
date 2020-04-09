package com.visumIT.Business.boost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;

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

    @Email
    @Column(name = "email", columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(name = "senha", columnDefinition = "VARCHAR(150)")
    private String senha;

    @Column(name="foto", columnDefinition = "VARCHAR(45)")
    private String foto;


}
