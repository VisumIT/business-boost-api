/*
* Author: Kaique
*
*  classe para tratar o login da empresa
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

import com.visumIT.Business.boost.DTO.EmpresaDTO;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.repository.EmpresaRepository;

@RestController
@RequestMapping("/empresa/login")
public class EmpresaLoginResource {

	@Autowired
	private EmpresaRepository empresaRepository;
	
	private EmpresaDTO dto = new EmpresaDTO();

	@PostMapping
	private ResponseEntity<Object> login(@RequestBody Empresa empresa) {
		Optional<Empresa> e = empresaRepository.findByemail(empresa.getEmail());
		if (e.isPresent()) {
			if (empresa.getSenha().equals(e.get().getSenha()) ) {
				EmpresaDTO dto2 = dto.toEmpresaDTO(e.get());
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
