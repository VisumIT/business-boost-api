package com.visumIT.Business.boost.teste;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Phone;

public class CompanyTeste {


	
	private List<Phone> phones  = new ArrayList<Phone>(); 
	
	private Company company;

	public CompanyTeste() {
		Phone phone = new Phone();
		phone.setNumber("1141419967");
		this.phones.add(phone);

		Company company = new Company();
		company.setPhones(phones);
		company.setFictitiousName("Mc Donalds");
		company.setEmail("mcxnlalds2@mcDonalds.com");
		company.setPassword("123456789");
		company.setDescription("comercio de alimentos");
		company.setCnpj("95.733.266/0001-50");
		company.setUf("SP");
		company.setCep("06414-070");
		company.setCity("Barueri");
		company.setNeighborhood("Jardim Tupanci");
		company.setSite("");
		company.setFictitiousName("");
		company.setAddress("Sol");
		company.setPublicPlace("Rua");
		company.setSite("mc.com");
		company.setNumber("1002");
		company.setStateRegistration("949.028.592.534");
		company.setCompanyName("ARCOS DOURADOS COMERCIO DE  ALIMENTOS LTDA");
		company.setPhones(this.phones);
		
		this.company = company;
	}

}
