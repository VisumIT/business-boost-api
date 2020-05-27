package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Marca;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


@RestController
@RequestMapping("/empresa/{id_empresa}/marca")
public class MarcaResource {

	@Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public ResponseEntity<?> getMarcas (@PathVariable(name="id_empresa") Long id ) {
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            Empresa empresa = new Empresa();
            empresa = empresa.optionalToEmpresa(empresaOptional);
            List<Marca> marcas = marcaRepository.findByEmpresa(empresa);

            return ResponseEntity.ok().body(marcas);
        }

        return ResponseEntity.notFound().build();

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> saveMarca(@PathVariable(name="id_empresa") Long id, @Valid @RequestBody Marca marca, BindingResult bindingResult) {
        /*garantindo que a empresa exista*/
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            Empresa empresa = new Empresa();
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
}
