package com.visumIT.Business.boost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Telefone;

public interface TelefoneRepository extends JpaRepository<Telefone, Long>{
}
