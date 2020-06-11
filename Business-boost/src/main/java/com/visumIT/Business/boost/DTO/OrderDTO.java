package com.visumIT.Business.boost.DTO;

import java.util.Optional;

import javax.validation.Valid;

import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Order;
import com.visumIT.Business.boost.models.Representative;

import lombok.Data;

@Data
public class OrderDTO {
	
	public OrderDTO(@Valid Order order2, Optional<Company> company, Optional<Representative> representative) {
		super();
		this.order = order2;
		this.company = company;
		this.representative = representative;
	}

	private @Valid Order order;
	
	private Optional<Company> company;
	
	private Optional<Representative> representative;
	
}
