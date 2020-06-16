package com.visumIT.Business.boost.models;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;




@SuppressWarnings("serial")
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_product")
public class Product implements Serializable{

	/**
	 * Wesley Meneghini
	 * Version 0.0.1
	 */
	private static final long serialVersionUID = -4242395355574778505L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 150)
	private String name;
	
	@NotNull
	private Double price;
	private Integer discount;
	private String brand;
	private String category;
	private String reference;
	private String deliveryTime;
	private String imageUrl = "product-no-photo.jpg";
	private Integer sold;
	
	@Column(name = "status")
	private String status;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	private Company company;
	
	@OneToOne(cascade = CascadeType.ALL)
	private ProductInformation productInformation;
	
	
}
