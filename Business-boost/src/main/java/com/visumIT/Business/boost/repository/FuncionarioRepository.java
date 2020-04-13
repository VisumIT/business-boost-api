package com.visumIT.Business.boost.repository;

import com.visumIT.Business.boost.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByMatricula(String matricula);
}
