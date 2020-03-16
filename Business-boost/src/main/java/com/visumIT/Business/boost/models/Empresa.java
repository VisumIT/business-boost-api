//classe representa o modelo da entidade empresa

package com.visumIT.Business.boost.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity(name ="Empresa")
@Table(name = "tbl_empresa")
@Data
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	//mappedBy indica o atributo da classe de destino
	//fetch eager carrega todos os endereços de uma vez, outra opção seria usar lazy para carregar por demanda
	@OneToMany(mappedBy = "empresa", fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Endereco> endereco;
	
	@Column(name = "inscricao_estadual", columnDefinition = "VARCHAR(20)")
	private String inscricaoEstadual;
	
	@Column(name = "razao", columnDefinition = "VARCHAR(60)")
	private String razaoSocial;
		
	@Column(name = "cnpj", columnDefinition = "VARCHAR(20)")
	private String cnpj;
	
	@Column(name = "nome_fantasia", columnDefinition = "VARCHAR(40)")
	private String nomeFantasia;
	
	@Column(name = "email", columnDefinition = "VARCHAR(40)")
	private String email;
	
	/*@Column(name = "telefone_opcao1", columnDefinition = "VARCHAR(15)")
	private String telefoneOpcao1;
	
	@Column(name = "telefone_opcao2", columnDefinition = "VARCHAR(15)")
	private String telefoneOpcao2;
	
	@Column(name = "telefone_opcao3", columnDefinition = "VARCHAR(15)")
	private String telefoneOpcao3;*/
		
	@Column(name = "senha", columnDefinition = "VARCHAR(150)")
	private String senha;
	
	@Column(name = "descricao", columnDefinition = "VARCHAR(200)")
	private String descricao;
	
	@Column(name = "logo", columnDefinition = "VARCHAR(40)")
	private String logo;
	
	@Column(name = "imagem", columnDefinition = "VARCHAR(40)")
	private String imagem;
}
