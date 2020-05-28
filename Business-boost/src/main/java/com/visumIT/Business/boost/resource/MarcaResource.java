package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Marca;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.MarcaRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


@RestController
@RequestMapping("/empresas/{id_empresa}/marcas")
public class MarcaResource {

	@Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;
    
    private
    Empresa empresa = new Empresa();

    //listar marcas de uma empresa
    @GetMapping
    public ResponseEntity<?> getMarcas (@PathVariable(name="id_empresa") Long id ) {
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            empresa = empresa.optionalToEmpresa(empresaOptional);
            List<Marca> marcas = marcaRepository.findByEmpresa(empresa);

            return ResponseEntity.ok().body(marcas);
        }

        return ResponseEntity.notFound().build();

    }
    //adicionar uma marca
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> saveMarca(@PathVariable(name="id_empresa") Long id, @Valid @RequestBody Marca marca, BindingResult bindingResult) {
        /*garantindo que a empresa exista*/
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            empresa = empresa.optionalToEmpresa(empresaOptional);
            
            List<Marca> marcas = empresa.getMarca();
            marcas.add(marca);
            marca.setEmpresa(empresa);
            empresa.setMarca(marcas);
            marcaRepository.save(marca);
            empresaRepository.save(empresa);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(marcas);
            
        }else if(bindingResult.hasErrors()) {
        	return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(marca);
    }
    //Atualizar uma marca
    @PutMapping("/{id_marca}")
    public ResponseEntity<?> updateMarca(@PathVariable(name="id_empresa")Long id_empresa,
    		@PathVariable(name="id_marca")Long id_marca, @RequestBody Marca marca, BindingResult bindingResult){
    	
    	if(bindingResult.hasErrors()) {
        	return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
    	}
    	else if(empresaRepository.existsById(id_empresa) && marcaRepository.existsById(id_marca)) {
    		Optional<Empresa> empresaOptional = empresaRepository.findById(id_empresa);
    		empresa = empresa.optionalToEmpresa(empresaOptional);
    		marca.setEmpresa(empresa);
    		
    		List<Marca> marcas = empresa.getMarca();
    		marcas.add(marca);
    		empresa.setMarca(marcas);
    		/*garantir que o id da marca não mude*/
    		marca.setId(id_marca);
    		marcaRepository.save(marca);
    		empresaRepository.save(empresa);
    		
    		return ResponseEntity.status(HttpStatus.CREATED).body(marca);
    		
    	}else {
    		return ResponseEntity.badRequest().body(new JSONObject().put("message", "Brand or Company not found").toString());
    	}
    	
    }
    @DeleteMapping("{id_marca}")
    public ResponseEntity<?> deleteMarca(@PathVariable(name="id_empresa")Long id_empresa,
    		@PathVariable(name="id_marca")Long id_marca){
    	if(empresaRepository.existsById(id_empresa) && marcaRepository.existsById(id_marca)) {
    		marcaRepository.deleteById(id_marca);
    		return ResponseEntity.noContent().build();
    	}else {
    		return ResponseEntity.badRequest().body(new JSONObject().put("message", "Brand or Company not found").toString());
    	}
    }
    
    
    
}
