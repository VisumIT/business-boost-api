package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Marca;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("empresa/{id_empresa}/marca")
public class MarcaResource {

	@Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private Empresa empresa;


    @GetMapping("/")
    public ResponseEntity<?> getMarcas (@PathVariable Long id ) {
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            empresa = empresa.optionalToEmpresa(empresaOptional);
            List<Marca> marcas = marcaRepository.findByEmpresa(empresa);

            return ResponseEntity.ok().body(marcas);
        }

        return ResponseEntity.notFound().build();

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public ResponseEntity<?> postMarca(@PathVariable Long id, @RequestBody Marca marca) {
        /*garantindo que a empresa exista*/
        if(empresaRepository.existsById(id)) {
            Optional<Empresa> empresaOptional = empresaRepository.findById(id);
            empresa = empresa.optionalToEmpresa(empresaOptional);
            List<Marca> marcas = empresa.getMarcas();
            marcas.add(marca);
            empresa.setMarcas(marcas);
            empresaRepository.save(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(marcas);
        }
            return ResponseEntity.notFound().build();
    }
}
