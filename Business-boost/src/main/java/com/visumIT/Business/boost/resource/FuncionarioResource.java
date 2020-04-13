package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Funcionario;
import com.visumIT.Business.boost.models.Representante;
import com.visumIT.Business.boost.models.Telefone;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.repository.FuncionarioRepository;
import com.visumIT.Business.boost.repository.TelefoneRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresa/{id}/funcionarios")
public class FuncionarioResource {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    //retorna os funcionarios de um empresa
    @GetMapping
    public ResponseEntity<?> getFuncionarios (@PathVariable Long id){
        Optional<Empresa> empresaProcurada = empresaRepository.findById(id);

        if(empresaProcurada.isPresent()){
            List<Funcionario> funcionarios = funcionarioRepository.findAll();
            funcionarios = empresaProcurada.get().getFuncionarios();
            return ResponseEntity.ok().body(funcionarios);
        }else
            return ResponseEntity.notFound().build();
    }

    //cadastro de um novo funcionario
    @PostMapping
    public ResponseEntity<?> gravarFuncionario(@Valid @RequestBody Funcionario funcionario,@PathVariable(name = "id") Long id_empresa, BindingResult bindingResult){
        if(funcionarioRepository.existsByEmail(funcionario.getEmail())){
            return ResponseEntity.badRequest().body(new JSONObject()
                    .put("message", "E-mail allready in use").toString());
        }else if(funcionarioRepository.existsByMatricula(funcionario.getMatricula())){
            return ResponseEntity.badRequest().body(new JSONObject()
                    .put("message", "Matricula allready in use")
                    .toString());
        }else if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));
        }else{
            funcionarioRepository.save(funcionario);
            Optional<Empresa> empresaOptional = empresaRepository.findById(id_empresa);

            if(empresaOptional.isPresent()){
                Empresa empresa = new Empresa();
                funcionario.setEmpresa(empresa.optionalToEmpresa(empresaOptional));

                for (Telefone tel : funcionario.getTelefone()) {
                    tel.setFuncionario(funcionario);
                    telefoneRepository.save(tel);
                }
                funcionarioRepository.save(funcionario);
                return ResponseEntity.status(HttpStatus.CREATED).body(funcionario);
            }else
                return ResponseEntity.badRequest().build();
        }
    }
}
