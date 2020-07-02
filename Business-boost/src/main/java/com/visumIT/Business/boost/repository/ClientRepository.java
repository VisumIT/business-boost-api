package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
	boolean existsById(Long id);
	Client findClientById(Long id);
	Boolean existsByEmail(String email);
}
