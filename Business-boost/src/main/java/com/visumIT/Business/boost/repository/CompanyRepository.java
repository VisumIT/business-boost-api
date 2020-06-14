package com.visumIT.Business.boost.repository;
//interface para implementar os métodos do crud do jpa


import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Product;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	Optional <Company> findByemail(String email);
	Boolean existsByEmail(String email);
	Boolean existsByCnpj(String cnpj);

}
