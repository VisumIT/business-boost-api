package com.visumIT.Business.boost.DTO;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.cloud.Date;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Order;
import com.visumIT.Business.boost.models.OrderItem;
import com.visumIT.Business.boost.models.Representative;

import lombok.Data;

@Data
public class OrderDTO {
	
	private Long id;
	private Long representante;
	private Long clientId;
	private Long dicountId;
	private String status;
	private Double totalPrice;
	private Date createDate;
	private List<OrderItem> items;
	@JsonIgnoreProperties({"product"})
	private Company company;

	
	
	
}
