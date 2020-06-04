package com.visumIT.Business.boost.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tbl_order")
@Data
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long companyId;
	
	private Long representativeId;
	
	private Long clientId;
	
	private Double dicountId;
	
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long number;
	
	private String status;
	
	private Calendar createDate = Calendar.getInstance();

	
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<OrderItem>();
	
}
