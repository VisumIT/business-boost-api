package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
