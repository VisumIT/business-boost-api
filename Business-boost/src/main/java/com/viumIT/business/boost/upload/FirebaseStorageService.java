package com.viumIT.business.boost.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.api.services.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.internal.FirebaseAppStore;
import com.google.firebase.internal.FirebaseService;

@Service
public class FirebaseStorageService {
	
	@PostConstruct
	private void init() throws IOException {
		
		if(FirebaseApp.getApps().isEmpty()) {
			
			// ler o arquivo de configuração da conta
			InputStream serviceAccount = 
					FirebaseService
					.class
					.getResourceAsStream("/teste-ds3-5ded5-firebase-adminsdk-aganc-78ab6fc3d5.json");
			
			// definir os dados necessarios para acessar o storage
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
					  .setStorageBucket("teste-ds3-5ded5.appspot.com")
					  .build();
					
			FirebaseApp.initializeApp(options);
			
		}
		
	}
	
	public String upload(FileUpload file, String name) {
		
		// Criar um acesso ao bucket
		Bucket bucket = StorageClient.getInstance().bucket();
		
		// Pegar arquivo no formato base64 e converter ele novamente em bytes (arquivo)
		byte[] arquivo = Base64.getDecoder().decode(file.getBase64());
		
		// Criar o arquivo com os dados fornecidos
		Blob blob = bucket.create(name, arquivo, file.getMimetype());
		// Configurar uma regra para que o arquivo possa ser lido
		blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		
		String url = "https://storage.googleapis.com/" + bucket.getName() + "/" + name;
		return url;	
		
	}
	
	public boolean delete(String file) {	
		Bucket bucket = StorageClient.getInstance().bucket();
		boolean arquivo = bucket.getStorage().delete("teste-ds3-5ded5.appspot.com",file);
		return arquivo;
	}

}