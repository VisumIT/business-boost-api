package com.visumIT.Business.boost.repository;
//interface para implementar os métodos do crud do jpa


import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

}
