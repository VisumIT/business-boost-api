package com.visumIT.Business.boost;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.visumIT.Business.boost.resource.CompanyResource;

public class LoginResourceTest extends BusinessBoostApplicationTests {

	private MockMvc mockMvc;
	
	@Autowired
	private CompanyResource companyResource;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(companyResource).build();
	}
	
	@Test
	public void fazerLoginEmpresaDadosCorretos_RetornarStatusCode200() throws Exception {

		JSONObject jsonObj = null;
		jsonObj = new JSONObject().put("email", "treinamento@catarinaegabrielafotografiasltda.com.br");
		jsonObj.put("password", "12345678");
		String json = jsonObj.toString();
		System.out.println(json);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
