package com.visumIT.Business.boost.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Data
@Table(name = "tbl_orders")
public class Order implements Serializable{
	
	/**
	 * Wesley Meneghini
	 * Version 0.0.1
	 */
	private static final long serialVersionUID = 1453613543921637790L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	//@JsonIgnoreProperties({"product", "brand"})
	@ManyToOne
	@JoinColumn(nullable = false)
	private Company company;
	
	private Long representativeId;
	private Long clientId;
	private Double dicountId;
	private String status;
	private Double totalPrice;
	private Double discountPrice;
	private Double priceToPay;
	private Calendar createDate = Calendar.getInstance();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<OrderItem>();
	
	@PrePersist
    public void prePersist(){
        items.forEach( i -> i.setOrder(this));
    }
	
	
}
