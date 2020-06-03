package com.visumIT.Business.boost.resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.repository.CompanyRepository;


@RestController
public class ImageResource {
	
	private String uploadedFolder = "/home/karl/TCC/Desenvolvimento/tmp/imagens/";
	
	private ArrayList<String> lista= new ArrayList<>();

	@Autowired
	private CompanyRepository companyRepository;
	
	public Path uploadImage(MultipartFile file,
			RedirectAttributes redirectAttributes) {
	
		lista.add("image/png");
		lista.add("image/jpg");
		
		String imageType = file.getContentType().toString();

		if(file.isEmpty() || !lista.contains(imageType) ){
			Path path=Paths.get("404");
			return path;
		}
		try {
			byte[] bytes = file.getBytes();
			Calendar calendar = Calendar.getInstance();
			String name = calendar.getTimeInMillis() +file.getOriginalFilename();
			Path path = Paths.get(uploadedFolder + name);
			Files.write(path, bytes);
			return path;
		}catch(IOException e) {
			e.printStackTrace();
		}
		Path path=Paths.get("404");
		return path;
	}
	@PostMapping("/companiess/logo/{id}")
	public ResponseEntity<?> uploadLogo(@RequestBody MultipartFile file,
			RedirectAttributes redirectAttributes,@PathVariable Long id) {
	
		Path path = uploadImage(file, redirectAttributes);
		if(path.equals("404")) {
			return ResponseEntity.badRequest().body(new JSONObject().put("message", "Invalid file")
					.toString());			
		}
		//verifica se a companies existe
		
		if(companyRepository.existsById(id)) {
			Company companies = new Company();
			Optional<Company> companiesOptional = companyRepository.findById(id);
			companies = companies.optionalToCompany(companiesOptional);
			companies.setLogo(path.toString());
			companyRepository.save(companies);
			return ResponseEntity.ok(path);
		}
		return ResponseEntity.badRequest().build();
				

	}
	
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
