package com.visumIT.Business.boost.resource;

import com.visumIT.Business.boost.exception.ValidationFormat;
import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.repository.EmpresaRepository;
import com.visumIT.Business.boost.storage.Disco;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@RestController
@RequestMapping("/imagens")
public class ImagemResource {

    private String path;

    @Autowired
    Disco disco;

    @Autowired
    EmpresaRepository empresaRepository;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam MultipartFile imagem){
        path = disco.salvarImagem(imagem);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/empresa/{id}")
    public ResponseEntity<?> uploadEmpresa(@PathVariable Long id, @RequestParam MultipartFile imagem, BindingResult bindingResult ) {
        if (empresaRepository.existsById(id)) {
            Optional<Empresa> emp = empresaRepository.findById(id);
            path = disco.salvarImagem(imagem);
            emp.get().setImagem(path);
            Empresa empresa = new Empresa();
            empresaRepository.save(empresa.optionalToEmpresa(emp));

            if (bindingResult.hasErrors())
                return ResponseEntity.badRequest().body(ValidationFormat.formatarErros(bindingResult));

            else
                return ResponseEntity.ok().build();

        } else
           return ResponseEntity.badRequest().body(new JSONObject().put("message", "Upload fail").toString());
    }
}
