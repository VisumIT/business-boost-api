package com.visumIT.Business.boost.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;


@Entity
@Table(name = "tbl_product")
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Size(min = 2, max = 255, message = "the name must contain 3 to 255 characters")
	private String name;
	
	//@NotNull
	private String brand;
	
	//@NotNull
	private String category;
	
	//@NotNull
	private String reference;
		
	private String deliveryTime;
	
	private String imageUrl = "product-no-photo.jpg";
	
	private boolean status = true;
	
	@OneToOne(cascade = CascadeType.ALL)
	private ProductInformation productInformation;
	
}
