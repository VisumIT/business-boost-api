package com.visumIT.Business.boost.repository;

import com.visumIT.Business.boost.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByRegistration(String registration);
}
