package com.visumIT.Business.boost;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.resource.CompanyResource;

public class CompanyResourceTest extends BusinessBoostApplicationTests{
	
	private MockMvc mockMvc;
	
	@Autowired
	private CompanyResource companyResource;
	
	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(companyResource).build();
	}
	
	@Test
	public void criarCompanyDadosCorretos_RetornarStatusCode201() throws Exception {
		Company company = new Company("Rua Roque Alves Mendes", "Rua", "301", "SP", "Jardim Belval", "Cotia", "06657-600","924.435.185.839", "Catarina e Gabriela Fotografias Ltda", "45.643.446/0001-72", "foto show", "treinamento@catarinaegabrielafotografiasltda.com.br", bCryptEncoder.encode("12345678"), "empresa de fotos");
	
		ObjectMapper mapper = new ObjectMapper();
		
		String json = mapper.writeValueAsString(company);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/companies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	
	}
}
