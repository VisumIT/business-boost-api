package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Representative;

public interface RepresentativeRepository extends JpaRepository<Representative, Long> {
	Boolean existsByEmail(String email);
	Boolean existsByCpf(String cpf);
	Representative findByEmail(String email);
}
