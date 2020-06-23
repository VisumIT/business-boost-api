package com.visumIT.Business.boost.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.repository.EmployeeRepository;
import com.visumIT.Business.boost.security.UserSS;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {	
		Employee employee = employeeRepository.findByemail(email);
		if(employee==null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(employee.getId(), employee.getEmail(), employee.getPassword(),employee.getProfiles());
	}
	
}
