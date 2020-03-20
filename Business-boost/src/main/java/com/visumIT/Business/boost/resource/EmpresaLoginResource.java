package com.visumIT.Business.boost.resource;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.repository.EmpresaRepository;

@RestController
@RequestMapping("/empresa/login")
public class EmpresaLoginResource {

	@Autowired
	private EmpresaRepository empresaRepository;

	@PostMapping
	private Boolean login(@Valid @RequestBody Empresa empresa) {
		Optional<Empresa> e = empresaRepository.findByemail(empresa.getEmail());
		if (e.isPresent()) {
			if (empresa.getSenha().equals(e.get().getSenha()) ) {
				ResponseEntity.ok().build();
				return true;
			} else {
				ResponseEntity.badRequest();
				return false;
			}
		} else {
			ResponseEntity.notFound().build();
			return false;
		}

	}
}
