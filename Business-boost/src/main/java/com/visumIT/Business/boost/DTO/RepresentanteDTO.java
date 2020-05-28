/*
* Author: Kaique
* classe para tratar o retorno do representante sem comprometer informações sensiveis
* */

package com.visumIT.Business.boost.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RepresentanteDTO {
	
	private Long id;		
	
	private List<Telefone> telefone = new  ArrayList<>();
	
	private String cpf;
	
	private String email;
	
	private String descricao;
	
	private String foto;
	
	private String nome;
	
	private Date dataNascimento;
	
	private List<EmpresaDTO> empresas = new ArrayList<>();
	

	
	public RepresentanteDTO toRepresentanteDTO( Representante representante) {	
		RepresentanteDTO dto = new RepresentanteDTO();
		EmpresaDTO empresaDTO = new EmpresaDTO();	
		dto.setNome(representante.getNome());
		dto.setId(representante.getId());
		dto.setCpf(representante.getCpf());
		dto.setDescricao(representante.getDescricao());
		dto.setEmail(representante.getEmail());
		dto.setTelefone(representante.getTelefone());	
		dto.setFoto(representante.getFoto());
		dto.setDataNascimento(representante.getDataNascimento());
		
		dto.setEmpresas(empresaDTO.toEmpresasDTO(representante.getEmpresas()));
		return dto;
	}


	public List<RepresentanteDTO> toRepresentantesDTO(List<Representante> representantes) {
		//criando o array de empresas dto 
		RepresentanteDTO dto = new RepresentanteDTO();
		List<RepresentanteDTO> representantesDTO = new ArrayList<>();
		for(Representante representante: representantes) {			
			representantesDTO.add(dto.toRepresentanteDTO(representante));
		}
		return representantesDTO;
	}	
	
    public RepresentanteDTO optionalToRepresentanteDTO(Optional<Representante> optional){
    	RepresentanteDTO dto = new RepresentanteDTO();
    	EmpresaDTO dtoEmpresa = new EmpresaDTO();
    	
    	dto.setId(optional.get().getId());
    	dto.setEmail(optional.get().getEmail());
    	dto.setEmpresas(dtoEmpresa.toEmpresasDTO(optional.get().getEmpresas()));
    	dto.setFoto(optional.get().getFoto());
    	dto.setNome(optional.get().getNome());
    	dto.setTelefone(optional.get().getTelefone());
    	dto.setCpf(optional.get().getCpf());
    	dto.setDescricao(optional.get().getDescricao());
    	dto.setDataNascimento(optional.get().getDataNascimento());
    	
    	return dto;
    }
	
	
	//Construtor
	public RepresentanteDTO() {}


//******************************************GETTERS e SETTERS***********************/
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public List<Telefone> getTelefone() {
		return telefone;
	}



	public void setTelefone(List<Telefone> telefone) {
		this.telefone = telefone;
	}



	public String getCpf() {
		return cpf;
	}



	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public String getFoto() {
		return foto;
	}



	public void setFoto(String foto) {
		this.foto = foto;
	}



	public List<EmpresaDTO> getEmpresas() {
		return empresas;
	}



	public void setEmpresas(List<EmpresaDTO> empresas) {
		this.empresas = empresas;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Date getDataNascimento() {
		return dataNascimento;
	}


	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	
	
	
}
