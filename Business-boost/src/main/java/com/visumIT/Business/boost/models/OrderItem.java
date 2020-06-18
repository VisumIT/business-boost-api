package com.visumIT.Business.boost.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.mapping.Table.ForeignKeyKey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@Data
@Table(name = "tbl_teste")
public class OrderItem implements Serializable{

	/**
	 * Wesley Meneghini
	 */
	private static final long serialVersionUID = -8431513671686721324L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double price;
	private int quantity;
	
	/*@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_product")*/
	private Long productId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;

}
