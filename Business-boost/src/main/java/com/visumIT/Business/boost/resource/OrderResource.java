package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.models.Order;
import com.visumIT.Business.boost.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderResource {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@GetMapping
	public List<Order> getOrders(){
		return orderRepository.findAll();
	}
	
	
	/*@GetMapping("/company/{id}")
	public List<Order> getOrdersCompany(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByCompanyId(id);
		return orders;
	}*/
	
	@GetMapping("/representantive/{id}")
	public List<Order> getOrdersRepresentantive(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByRepresentativeId(id);
		return orders;
	}
	
	@GetMapping("/client/{id}")
	public List<Order> getOrdersClientId(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByClientId(id);
		return orders;
	}
	
	
	@PostMapping
	public Order create(@Valid @RequestBody Order order) {
		/*Long companyid =  order.getCompanyId();
		System.out.println(companyid);
		
		if(orderRepository.findById(companyid).isPresent()) {
			return order;
		}	*/
		
		return orderRepository.save(order);
	}
	
	
}





