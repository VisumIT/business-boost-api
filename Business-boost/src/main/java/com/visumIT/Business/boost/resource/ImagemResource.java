package com.visumIT.Business.boost.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class ImagemResource {
	
	private String uploadedFolder = "/home/karl/TCC/Desenvolvimento/tmp/imagens/";

	private String[] types = {"image/jpg","image/png"};

	
	
	public Boolean contain(String e, String[] types) {
		for(int i = 0; i<types.length;++i) {
			if(e==types[i]) {
				return true;
			}
		}return false;
		
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> singleFileUpload(@RequestBody MultipartFile file, RedirectAttributes redirectAttributes) {
	

		String imageType = file.getContentType().toString();
		if(!contain(imageType,types)){
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "tipo errado").toString());
		}
		
		if(file.isEmpty() ){
			redirectAttributes.addFlashAttribute("message", "Please select a image to upload");
			return ResponseEntity.badRequest().body(imageType);
			//return ResponseEntity.badRequest().body(new JSONObject().put("message", "Please select a image to upload").toString());
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadedFolder + file.getOriginalFilename());
			Files.write(path, bytes);
			
			redirectAttributes.addFlashAttribute("messsage", "You successfully uploaded: '" + file.getOriginalFilename()+ "'");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
