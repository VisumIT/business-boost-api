package com.visumIT.Business.boost.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class Image {
	
	private String uploadedFolder = "/home/karl/TCC/Desenvolvimento/tmp/imagens/";

	public String singleFileUpload(@RequestBody MultipartFile file, RedirectAttributes redirectAttributes) {
		
		if(file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus: ";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadedFolder + file.getOriginalFilename());
			Files.write(path, bytes);
			
			redirectAttributes.addFlashAttribute("messsage", "You successfully uploaded: '" + file.getOriginalFilename()+ "'");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/uploadStatus";
	}
	
	public String uploadStatus() {
		return "uploadStatus";
	}
}

