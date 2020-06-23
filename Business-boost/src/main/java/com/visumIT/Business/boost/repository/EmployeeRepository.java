package com.visumIT.Business.boost.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByRegistration(String registration);
    Boolean existsByCompany(Company company);
    Employee findByemail(String email);
    
}
