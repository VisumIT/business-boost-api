/*
* Author: Kaique
*
*  classe para tratar o login da company
* */

package com.visumIT.Business.boost.resource;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.DTO.CompanyDTO;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.repository.CompanyRepository;

@RestController
@RequestMapping("/company/login")
public class CompanyLoginResource {

	@Autowired
	private CompanyRepository companyRepository;
	
	private CompanyDTO dto = new CompanyDTO();

	@PostMapping
	private ResponseEntity<Object> login(@RequestBody Company company) {
		Optional<Company> e = companyRepository.findByemail(company.getEmail());
		if (e.isPresent()) {
			if (company.getPassword().equals(e.get().getPassword()) ) {
				CompanyDTO dto2 = dto.toCompanyDTO(e.get());
				return ResponseEntity.ok().body(dto2);
			} else {
				return ResponseEntity.badRequest()
						.body(new JSONObject()
						.put("message", "Incorrect login or password")
						.toString());
			}
		} else {
			ResponseEntity.notFound().build();
			return ResponseEntity.badRequest()
					.body(new JSONObject()
					.put("message", "Incorrect login or password")
					.toString());
		}

	}
}
