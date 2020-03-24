//classe representa o modelo da entidade empresa

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Constraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Data;

@Entity(name ="Empresa")
@Table(name = "tbl_empresa")
@Data
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//endereco
	@Size(max = 150)
	@Column(name="endereco", columnDefinition = "VARCHAR(150)")
	private String endereco;
	
	@Column(name="logradouro", columnDefinition = "VARCHAR(20)")
	private String logradouro;
	
	@Column(name="numero", columnDefinition = "VARCHAR(10)")
	private String numero;
	
	@Column(name="uf", columnDefinition = "VARCHAR(20)")
	private String uf;
	
	@Column(name="bairro", columnDefinition = "VARCHAR(50)")
	private String bairro;
	
	@Column(name="cidade", columnDefinition = "VARCHAR(50)")
	private String cidade;
	
	@Column(name="cep", columnDefinition = "VARCHAR(20)")
	private String cep;
	//********************
	
	@Column(name="site", columnDefinition = "VARCHAR(100)") 
	private String site;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
	@Column(name = "telefone", columnDefinition = "VARCHAR(20)")
	private List<Telefone> telefone = new  ArrayList<>();
	
	@Column(name = "inscricao_estadual", columnDefinition = "VARCHAR(20)")
	private String inscricaoEstadual;
	
	@Column(name = "razao", columnDefinition = "VARCHAR(60)")
	private String razaoSocial;
	
	@NotBlank(message="{cnpj.not.blank}")
	@CNPJ(message="{CNPJ.empresa.cnpj}")
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)")
	private String cnpj;
	
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
	
	@Column(name = "logo", columnDefinition = "VARCHAR(40)")
	private String logo;
	
	@Column(name = "imagem", columnDefinition = "VARCHAR(40)")
	private String imagem;

}
