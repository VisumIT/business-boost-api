package com.visumIT.Business.boost.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tbl_endereco")
@Data
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "endereco", columnDefinition = "VARCHAR(40)")
	private String endereco;

	@Column(name = "logradouro", columnDefinition = "VARCHAR(10)")
	private String logradouro;
	
	@ManyToOne
	@JoinColumn(name="empresa_id")
	private Empresa empresa;
	
	@Column(name = "bairro", columnDefinition = "VARCHAR(40)")
	private String bairro;

	@Column(name = "cidade", columnDefinition = "VARCHAR(40)")
	private String cidade;

	@Column(name = "uf", columnDefinition = "VARCHAR(10)")
	private String uf;

	@Column(name = "pais", columnDefinition = "VARCHAR(40)")
	private String pais;

	@Column(name = "numero", columnDefinition = "VARCHAR(40)")
	private String numero;

	@Column(name = "cep", columnDefinition = "VARCHAR(40)")
	private String cep;

}
