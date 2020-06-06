package com.visumIT.Business.boost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long>{
}
