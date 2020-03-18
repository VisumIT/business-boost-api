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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="tbl_telefone")
@Data
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="numero")
	private String numero;
	
	@JoinColumn(name = "empresa_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Empresa empresa;
}
