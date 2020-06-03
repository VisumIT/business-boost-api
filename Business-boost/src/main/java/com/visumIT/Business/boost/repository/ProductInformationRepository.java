package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.ProductInformation;

public interface ProductInformationRepository extends JpaRepository<ProductInformation, Long>{

}
