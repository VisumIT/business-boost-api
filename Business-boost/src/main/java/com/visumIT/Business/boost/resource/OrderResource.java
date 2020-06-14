package com.visumIT.Business.boost.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visumIT.Business.boost.DTO.OrderDTO;
import com.visumIT.Business.boost.models.Company;
import com.visumIT.Business.boost.models.Order;
import com.visumIT.Business.boost.models.OrderItem;
import com.visumIT.Business.boost.models.Phone;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.OrderItemRepository;
import com.visumIT.Business.boost.repository.OrderRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;

@RestController
@RequestMapping("/")
public class OrderResource {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private RepresentativeRepository representativeRepository;
	
	@GetMapping("/orders")
	public List<Order> getOrders(){
		return orderRepository.findAll();
	}
	
	
	/*@GetMapping("/orders/company/{id}")
	public List<Order> getOrdersCompany(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByCompabyId(id);
		return orders;
	}*/
	
	/*@GetMapping("/orders/representantive/{id}")
	public List<Order> getOrdersRepresentantive(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByRepresentative(id);
		return orders;
	}
	
	@GetMapping("/orders/client/{id}")
	public List<Order> getOrdersClientId(@PathVariable Long id){
		List<Order> orders = orderRepository.findAllByClientId(id);
		return orders;
	}*/
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@PostMapping("/company/{idCompany}/representantive/{idRepresentantive}/client/{idClient}/orders")
	public ResponseEntity<?> create(
						@PathVariable("idCompany") Long idCompany,
						@PathVariable("idRepresentantive") Long idRepresentantive,
						@PathVariable("idClient") Long idClient,
						@RequestBody Order order) {
		Optional<Company> company = companyRepository.findById(idCompany);
				
		System.out.println("idCompany -> " + idCompany);
		if(company.isEmpty()) {
			System.out.println("Nao encontrou nenhuma empresa");
			return ResponseEntity.notFound().build();
		}
		
		System.out.println("1");
		
		// Atualizar a chave estrangeira no banco de dados
		Order orderSave = orderRepository.save(order);
		for (OrderItem item : orderSave.getItems()) {
			item.setOrder(orderSave);
			orderItemRepository.save(item);
			System.out.println("2");
		}
		System.out.println("3");
		
		Optional<Order> res = orderRepository.findById(orderSave.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
		/*Optional<Company> company = companyRepository.findById(idCompany);
		
		System.out.println("idCompany -> " + idCompany);
		if(company.toString() == null) {
			System.out.println("sem empresa");
			return ResponseEntity.notFound().build();
		}
		
		Optional<Representative> representative = representativeRepository.findById(idRepresentantive);
		
		System.out.println("idRepresentantive -> " + idRepresentantive);
		if(company.toString() == null) {
			System.out.println("sem representante");
			return ResponseEntity.notFound().build();
		}
		
		OrderDTO order1 = new OrderDTO(order, company, representative);
		
		return ResponseEntity.ok(order1);*/
	}
	
	
}





