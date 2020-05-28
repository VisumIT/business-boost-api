/*
*Author: Kaique
*
*classe representa o modelo da entidade empresa
*/
package com.visumIT.Business.boost.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import lombok.Data;
@Data
@Entity(name ="Empresa")
@Table(name = "tbl_empresa")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//representante
	@ManyToMany(mappedBy = "empresas" )
	@JsonIgnore
	private List<Representante> representantes = new ArrayList<>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
	@Column(name = "telefone", columnDefinition = "VARCHAR(20)")
	private List<Telefone> telefone = new  ArrayList<>();

	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
	private List<Marca> marca = new  ArrayList<>();

	//endereco
	@Size(max = 150)
	@Column(name="endereco", columnDefinition = "VARCHAR(150)")
	private String endereco;

	@Size(min = 3, max = 20)
	@Column(name="logradouro", columnDefinition = "VARCHAR(20)")
	private String logradouro;

	@Size(max = 10)
	@Column(name="numero", columnDefinition = "VARCHAR(10)")
	private String numero;

	@Size(min = 2)
	@Column(name="uf", columnDefinition = "VARCHAR(20)")
	private String uf;

	@Size(max = 50)
	@Column(name="bairro", columnDefinition = "VARCHAR(50)")
	private String bairro;

	@Size(max = 50)
	@Column(name="cidade", columnDefinition = "VARCHAR(50)")
	private String cidade;

	@Size(min = 8)
	@Column(name="cep", columnDefinition = "VARCHAR(20)")
	private String cep;
	//********************

	@Size(max = 100)
	@Column(name="site", columnDefinition = "VARCHAR(100)") 
	private String site;

	@Size(min = 14, max = 20)
	@NotBlank
	@Column(name = "inscricao_estadual", columnDefinition = "VARCHAR(20)")
	private String inscricaoEstadual;

	@Size(max = 60)
	@NotBlank
	@Column(name = "razao", columnDefinition = "VARCHAR(60)")
	private String razaoSocial;
	
	@NotBlank(message="{cnpj.not.blank}")
	@CNPJ(message="{CNPJ.empresa.cnpj}")
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)")
	private String cnpj;

	@Size(max = 40)
	@Column(name = "nome_fantasia", columnDefinition = "VARCHAR(40)")
	private String nomeFantasia;
	
	@NotBlank(message="{email.not.blank}")
	@Email(message="{email.not.valid}")
	@Column(name = "email", columnDefinition = "VARCHAR(40)", unique=true)
	private String email;
	
	@NotBlank(message="{senha.not.blank}")
	@Size(min = 8, message="{Size.empresa.senha}")
	@Column(name = "senha", columnDefinition = "VARCHAR(255)")
	private String senha;
	
	@Column(name = "descricao", columnDefinition = "VARCHAR(200)")
	private String descricao;
	
	@Column(name = "logo", columnDefinition = "VARCHAR(255)")
	private String logo;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
	@Column(name="funcionarios")
	private List<Funcionario> funcionarios = new  ArrayList<>();

	//transformar um tipo opcional no tipo empresa
	public Empresa optionalToEmpresa( Optional<Empresa> optional) {
			Empresa empresa = new Empresa();
			
			empresa.setId(optional.get().getId());
			empresa.setBairro(optional.get().getBairro());
			empresa.setCep(optional.get().getCep());
			empresa.setCidade(optional.get().getCidade());
			empresa.setCnpj(optional.get().getCnpj());
			empresa.setDescricao(optional.get().getDescricao());
			empresa.setEmail(optional.get().getEmail());
			empresa.setEndereco(optional.get().getEndereco());
			empresa.setInscricaoEstadual(optional.get().getInscricaoEstadual());
			empresa.setLogo(optional.get().getLogo());
			empresa.setLogradouro(optional.get().getLogradouro());
			empresa.setNomeFantasia(optional.get().getNomeFantasia());
			empresa.setNumero(optional.get().getNumero());
			empresa.setRazaoSocial(optional.get().getRazaoSocial());
			empresa.setRepresentantes(optional.get().getRepresentantes());
			empresa.setSenha(optional.get().getSenha());
			empresa.setSite(optional.get().getSite());
			empresa.setTelefone(optional.get().getTelefone());
			empresa.setUf(optional.get().getUf());
			
			return empresa;
	}



	/*getter and setters*/
	public List<Representante> getRepresentantes() {
		return representantes;
	}

	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}

	public void setRepresentantes(List<Representante> representantes) {
		this.representantes = representantes;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
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

	public List<Telefone> getTelefone() {
		return telefone;
	}

	public void setTelefone(List<Telefone> telefone) {
		this.telefone = telefone;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}




	public String getCnpj() {
		return cnpj;
	}




	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}




	public String getNomeFantasia() {
		return nomeFantasia;
	}




	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
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




	public String getDescricao() {
		return descricao;
	}




	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	public String getLogo() {
		return logo;
	}




	public void setLogo(String logo) {
		this.logo = logo;
	}


	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}




	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
}
