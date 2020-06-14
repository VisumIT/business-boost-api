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
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import lombok.Data;

@Entity
@Table(name = "tbl_orders")
@Data
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long company;
	private Long representativeId;
	private Long clientId;
	private Double dicountId;
	private String status;
	private Calendar createDate = Calendar.getInstance();
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private List<OrderItem> items = new ArrayList<OrderItem>();
	
}
