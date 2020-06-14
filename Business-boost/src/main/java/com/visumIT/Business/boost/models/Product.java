package com.visumIT.Business.boost.models;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tbl_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_product")
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 150)
	@Column(name = "name")
	private String name;
	
	@NotNull
	@Column(name = "price")
	private Double price;
	
	@Column(name = "discount")
	private Integer discount;
	
	//@OneToOne
	private String brand;
	
	//@NotNull
	private String category;
	
	//@NotNull
	private String reference;
		
	private String deliveryTime;
	
	private String imageUrl = "product-no-photo.jpg";
	
	@Column(name = "sold")
	private Integer sold;
	
	@Column(name = "status")
	private String status;
	
	@OneToOne(cascade = CascadeType.ALL)
	private ProductInformation productInformation;
	
	//@NotNull
	
	//@JsonIgnoreProperties({ "description","uf","representatives","publicPlace","number", "phones", "brand", "address", "employees", "password", "email", "cnpj", "" })
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ProductInformation getProductInformation() {
		return productInformation;
	}

	public void setProductInformation(ProductInformation productInformation) {
		this.productInformation = productInformation;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", discount=" + discount + ", brand="
				+ brand + ", category=" + category + ", reference=" + reference + ", deliveryTime=" + deliveryTime
				+ ", imageUrl=" + imageUrl + ", sold=" + sold + ", status=" + status + ", productInformation="
				+ productInformation + ", company=" + company + "]";
	}
	
	
	
}
