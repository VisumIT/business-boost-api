package com.visumIT.Business.boost.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Employee;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.EmployeeRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;
import com.visumIT.Business.boost.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private RepresentativeRepository representativeRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {	
		Employee employee = employeeRepository.findByemail(email);	
		Company company = companyRepository.findCompanyByemail(email);
		Representative representative = representativeRepository.findByEmail(email);
		if(employee !=null) {
			return new UserSS(employee.getId(), employee.getEmail(), employee.getPassword(),employee.getProfiles());
		}
		if(company!=null) {
			return new UserSS(company.getId(), company.getEmail(), company.getPassword(),company.getProfiles());
		}
		if(representative != null) {
			return new UserSS(representative.getId(), representative.getEmail(), representative.getPassword(),representative.getProfiles());
		}else {
			throw new UsernameNotFoundException(email);
		}
	}
}
