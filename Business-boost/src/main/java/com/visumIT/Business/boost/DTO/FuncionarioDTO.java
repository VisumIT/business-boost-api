package com.visumIT.Business.boost.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Funcionario;
import com.visumIT.Business.boost.models.Telefone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDTO {

    private Long id;

    @JsonIgnore
    private Empresa empresa;

    private String matricula;

    private String nome;

    private List<Telefone> telefone = new ArrayList<>();

    private String email;

    private String foto;

    //converte objeto do tipo funcionario para funcionarioDTO
    public FuncionarioDTO toFuncionarioDTO (Funcionario funcionario) {
        FuncionarioDTO dto = new FuncionarioDTO();
        
        dto.setEmail(funcionario.getEmail());
        dto.setEmpresa(funcionario.getEmpresa());
        dto.setFoto(funcionario.getFoto());
        dto.setId(funcionario.getId());
        dto.setMatricula(funcionario.getMatricula());
        dto.setNome(funcionario.getNome());
        dto.setTelefone(funcionario.getTelefone());

        return dto;
    }

    public List<FuncionarioDTO> toFuncionariosDTO(List<Funcionario> funcionarios){
        FuncionarioDTO dto = new FuncionarioDTO();
        List<FuncionarioDTO> funcionariosDTO = new ArrayList<>();
        for(Funcionario funcionario: funcionarios){
            funcionariosDTO.add(dto.toFuncionarioDTO(funcionario));
        }
        return funcionariosDTO;
    }
    
    public FuncionarioDTO optionalToFuncionarioDTO(Optional<Funcionario> optional){
    	FuncionarioDTO dto = new FuncionarioDTO();
    	
    	dto.setId(optional.get().getId());
    	dto.setEmail(optional.get().getEmail());
    	dto.setEmpresa(optional.get().getEmpresa());
    	dto.setFoto(optional.get().getFoto());
    	dto.setMatricula(optional.get().getMatricula());
    	dto.setNome(optional.get().getNome());
    	dto.setTelefone(optional.get().getTelefone());
    	
    	return dto;
    }
    
}
