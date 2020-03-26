package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Representante;

public interface RepresentanteRepository extends JpaRepository<Representante, Long> {
	Boolean existsByEmail(String email);
	Boolean existsByCpf(String cpf);
}
