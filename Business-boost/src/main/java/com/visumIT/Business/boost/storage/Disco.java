package com.visumIT.Business.boost.storage;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Disco {

    @Value("${empresa.disco.raiz}")
    private String raiz;

    @Value("${empresa.disco.diretorio-imagens}")
    private String diretorioImagens;

    public String salvarImagem(MultipartFile imagem) {
        return this.salvar(this.diretorioImagens, imagem);
    }

    public String salvar(String diretorio, MultipartFile arquivo){
        Path diretorioPath = Paths.get(this.raiz, diretorio);
        Path arquivoPath = diretorioPath.resolve(arquivo.getOriginalFilename());
        String path = raiz + "/" + diretorioImagens + "/" + arquivo.getOriginalFilename();
        try{
            Files.createDirectories(diretorioPath);
            arquivo.transferTo(arquivoPath.toFile());
            return path;
        }catch(IOException e){
            throw new RuntimeException("Incapaz de salvar o arquivo");
        }

    }


}


