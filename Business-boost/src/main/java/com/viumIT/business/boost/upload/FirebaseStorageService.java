package com.viumIT.business.boost.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {
	// executa só uma vez
	@PostConstruct
	private void init() throws IOException {
		if (FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME) != null) {
			try {
				if (FirebaseApp.getApps().isEmpty()) {
				}
				// ler o arquivo de configuração da conta
				// getResource sempre procura dentro de resource
				InputStream serviceAccount = FirebaseStorageService.class
						.getResourceAsStream("/teste-ds3-5ded5-firebase-adminsdk-aganc-eec7b193a6.json");
				
				// Definir dados necessários par aacessar o Storage
				FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setStorageBucket("teste-ds3-5ded5.appspot.com").build();
				// iniciar o serviço cliente firebase
				FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String upload(FileUpload file) {
		// criar um acesso ao bucket
		Bucket bucket = StorageClient.getInstance().bucket();

		// Transformar o base64 em bytes novamente (arquivo) desserializar
		byte[] arquivo = Base64.getDecoder().decode(file.getBase64());

		//garante um nome único para o arquivo
		Calendar calendar = Calendar.getInstance();
		String name = calendar.getTimeInMillis() +file.getFileName();
		// Criar arquivo com os dados
		Blob blob = bucket.create(name, arquivo, file.getMimetype());

		// Configurar uma regra para que o arquivo possa ser lido
		blob.createAcl(Acl.of(User.ofAllUsers(), Acl.Role.READER));

		return "https://storage.googleapis.com/" + bucket.getName() + "/" + file.getFileName();
	}

}
