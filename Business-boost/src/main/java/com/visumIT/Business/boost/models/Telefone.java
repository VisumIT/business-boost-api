package com.visumIT.Business.boost.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="tbl_telefone")
@Data
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 6, max = 20 )
	@Column(name="numero", columnDefinition = "VARCHAR(20)")
	private String numero;
	
	@JoinColumn(name = "empresa")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Empresa empresa;
	
	@JoinColumn(name = "representante_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Representante representante;

	@JoinColumn(name = "funcionario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Funcionario funcionario;
}
