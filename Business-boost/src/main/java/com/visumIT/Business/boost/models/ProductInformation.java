package com.visumIT.Business.boost.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name="tbl_product_information")
@Data
public class ProductInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String price;
	
	@NotNull
	private String stock;
	
	private String size;
	
	private String color;
	
	
	

	
	
	
}
