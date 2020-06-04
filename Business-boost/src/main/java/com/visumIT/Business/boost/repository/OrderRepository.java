package com.visumIT.Business.boost.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visumIT.Business.boost.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
	
	public List<Order> findAllByCompanyId(Long companyId);
	public List<Order> findAllByRepresentativeId(Long companyId);
	public List<Order> findAllByClientId(Long companyId);
}
