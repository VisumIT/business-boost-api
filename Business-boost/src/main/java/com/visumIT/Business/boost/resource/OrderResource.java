package com.visumIT.Business.boost.resource;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.visumIT.Business.boost.models.Product;
import com.visumIT.Business.boost.models.Representative;
import com.visumIT.Business.boost.repository.CompanyRepository;
import com.visumIT.Business.boost.repository.OrderItemRepository;
import com.visumIT.Business.boost.repository.OrderRepository;
import com.visumIT.Business.boost.repository.ProductRepository;
import com.visumIT.Business.boost.repository.RepresentativeRepository;

@RestController
@RequestMapping("")
public class OrderResource {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
		// Verificando empresa
		Optional<Company> company = companyRepository.findById(idCompany);
		
		if(!company.isPresent()) {
			return ResponseEntity.status(458).build();
		}
		order.setCompany(company.get());
		order.setRepresentativeId(1L);
		order.setClientId(1L);
		
		// Calcular preço total do pedido
		Double totalPrice = 0.0;
		for(int i = 0; i < order.getItems().size(); i++) {
			OrderItem orderItem = order.getItems().get(i);
			Optional<Product> product = productRepository.findById(orderItem.getProductId());
			Double priceItemQuantity = orderItem.getQuantity() * product.get().getPrice();
			totalPrice += priceItemQuantity;
		}
		
		//DecimalFormat df = new DecimalFormat("#0.00");
		order.setTotalPrice(totalPrice);
		order.setStatus("created");
		
		Order orderSave = orderRepository.save(order);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(orderSave);
		
		
		//return ResponseEntity.ok(null);
		
		/*for (OrderItem item : orderSave.getItems()) {
			item.setOrder(orderSave);
			orderItemRepository.save(item);
			
		}
		
		Order orderSave = orderRepository.save(order);
		
		
		/*for (OrderItem item : orderSave.getItems()) {
			item.setOrder(orderSave);
			orderItemRepository.save(item);
			System.out.println("2");
		}*/
		
		
		/*Optional<Order> res = orderRepository.findById(orderSave.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(res);*/
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





