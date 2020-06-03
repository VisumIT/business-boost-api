package com.visumIT.Business.boost.repository;
//interface para implementar os métodos do crud do jpa


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	Optional <Company> findByemail(String email);
	Boolean existsByEmail(String email);
	Boolean existsByCnpj(String cnpj);

}
