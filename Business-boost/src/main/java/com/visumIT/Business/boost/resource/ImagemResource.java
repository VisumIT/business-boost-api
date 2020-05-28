package com.visumIT.Business.boost.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class ImagemResource {
	
	private String uploadedFolder = "/home/karl/TCC/Desenvolvimento/tmp/imagens/";
	
	private ArrayList<String> lista= new ArrayList<>();
	
	//se a string recebida existir no array retorna verdadeiro
	public Boolean contain(String e, String[] types) {
		for(int i = 0; i<types.length;++i) {
			if(e.equals(types[i])) {
				return true;
			}
		}return false;
		
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> singleFileUpload(@RequestBody MultipartFile file, RedirectAttributes redirectAttributes) {
	

		lista.add("image/png");
		lista.add("image/jpg");
		
		String imageType = file.getContentType().toString();

		if(file.isEmpty() || !lista.contains(imageType) ){
			redirectAttributes.addFlashAttribute("message", "Please select a image to upload");
			//return ResponseEntity.badRequest().body(imageType);
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "Please select a image to upload").toString());
		}
		try {
			byte[] bytes = file.getBytes();
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() +file.getOriginalFilename();
			Path path = Paths.get(uploadedFolder + name);
			Files.write(path, bytes);
			return ResponseEntity.ok().body(path);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
