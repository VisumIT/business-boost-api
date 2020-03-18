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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.br.CNPJ;

import lombok.Data;

@Entity(name ="Empresa")
@Table(name = "tbl_empresa")
@Data
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	 
//	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
//	private List<Endereco> endereco = new ArrayList<>();
	
	@Size(max = 150)
	@Column(name="endereco", columnDefinition = "VARCHAR(150)")
	private String endereco;
	
	@Size( min = 9, max = 20 )
	@OneToMany(cascade=CascadeType.ALL, mappedBy = "empresa")
	@Column(name = "telefone", columnDefinition = "VARCHAR(20)")
	private List<Telefone> telefone = new  ArrayList<>();
	
	@Column(name = "inscricao_estadual", columnDefinition = "VARCHAR(20)")
	private String inscricaoEstadual;
	
	@Column(name = "razao", columnDefinition = "VARCHAR(60)")
	private String razaoSocial;
	
	@CNPJ
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)")
	private String cnpj;
	
	@Column(name = "nome_fantasia", columnDefinition = "VARCHAR(40)")
	private String nomeFantasia;
	
	@Column(name = "email", columnDefinition = "VARCHAR(40)")
	private String email;
		
	@Column(name = "senha", columnDefinition = "VARCHAR(150)")
	private String senha;
	
	@Column(name = "descricao", columnDefinition = "VARCHAR(200)")
	private String descricao;
	
	@Column(name = "logo", columnDefinition = "VARCHAR(40)")
	private String logo;
	
	@Column(name = "imagem", columnDefinition = "VARCHAR(40)")
	private String imagem;
}
