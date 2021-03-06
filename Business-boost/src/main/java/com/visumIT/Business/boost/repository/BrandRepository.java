package com.visumIT.Business.boost.repository;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository  extends JpaRepository<Brand, Long> {
    List<Brand> findByCompany(Company company);
    Boolean existsByCompany(Company company);
}
