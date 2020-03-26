/* Author: kaique
 * está classe tem o objetivo de retornar os dados da empresa sem comprometer a senha ou seja não retorna a 
 * senha da empresa
 * Alterado:
 * */

package com.visumIT.Business.boost.DTO;

import java.util.ArrayList;
import java.util.List;

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Telefone;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpresaDTO {


	private Long id;
	
	//endereco
	private String endereco;
	
	private String logradouro;
	
	private String numero;
	
	private String uf;
	
	private String bairro;
	
	private String cidade;
	
	private String cep;
	//********************
	
	private String site;
	
	private List<Telefone> telefone = new  ArrayList<>();
	
	private String inscricaoEstadual;
	
	private String razaoSocial;
	
	private String cnpj;
	
	private String nomeFantasia;
	
	private String email;
	
	private String descricao;
	
	private String logo;
	
	private String imagem;
	
	public EmpresaDTO() {}
	
	//método converte um obj do tipo empresa para empresaDTO
	public EmpresaDTO toEmpresaDTO( Empresa empresa) {
		EmpresaDTO dto = new EmpresaDTO();
		dto.setId(empresa.getId());
		dto.setBairro(empresa.getBairro());
		dto.setCep(empresa.getCep());
		dto.setCidade(empresa.getCidade());
		dto.setCnpj(empresa.getCnpj());
		dto.setDescricao(empresa.getDescricao());
		dto.setEmail(empresa.getEmail());
		dto.setEndereco(empresa.getEndereco());
		dto.setImagem(empresa.getImagem());
		dto.setInscricaoEstadual(empresa.getInscricaoEstadual());
		dto.setLogo(empresa.getLogo());
		dto.setLogradouro(empresa.getLogradouro());
		dto.setNomeFantasia(empresa.getNomeFantasia());
		dto.setNumero(empresa.getNumero());
		dto.setRazaoSocial(empresa.getRazaoSocial());
		dto.setSite(empresa.getSite());
		dto.setTelefone(empresa.getTelefone());
		dto.setUf(empresa.getUf());
		
		return dto;
	}
	
	public List<EmpresaDTO> toEmpresasDTO(List<Empresa> empresas) {
		//criando o array de empresas dto 
		EmpresaDTO dto = new EmpresaDTO();
		List<EmpresaDTO> empresasDTO = new ArrayList<>();
		for(Empresa empresa: empresas) {			
			empresasDTO.add(dto.toEmpresaDTO(empresa));
		}
		return empresasDTO;
	}
 	
}
