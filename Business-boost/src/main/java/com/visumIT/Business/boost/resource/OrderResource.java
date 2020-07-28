package com.visumIT.Business.boost.resource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PrePersist;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@CrossOrigin
public class OrderResource {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RepresentativeRepository representativeRepository;
	
	DecimalFormat df = new DecimalFormat();
	
	@GetMapping("/orders")
	public List<Order> getOrders(){
		return orderRepository.findAll();
	}
	
	@GetMapping("/orders/{idOrder}")
	public ResponseEntity<?> getOrdersById(@PathVariable Long idOrder){
		Order order = orderRepository.findById(idOrder).orElse(null);
		if(order == null) return ResponseEntity.badRequest().build();
		
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/company/{idCompany}/orders")
	public ResponseEntity<?> getOrdersByCOmpany(@PathVariable Long idCompany){
		Optional<Company> company = companyRepository.findById(idCompany);
        
        if(!company.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
         
		return ResponseEntity.ok(company.get().getOrders());
	}
	
	
	
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
	
	
	
	/*@PostMapping("/company/{idCompany}/representantive/{idRepresentantive}/client/{idClient}/orders")
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
		order.setStatus("created");
		
		Order orderSave = orderRepository.save(order);
		
		// Calcular preço total do pedido
		Double totalPrice = 0.0;
		
		
		List<OrderItem> items = new ArrayList<OrderItem>();
		
		for(int i = 0; i < order.getItems().size(); i++) {
			
			OrderItem orderItem = order.getItems().get(i);
			Optional<Product> product = productRepository.findById(orderItem.getProductId());
			
			orderItem.setPrice(product.get().getPrice());
			orderItem.setOrder(orderSave);
			
			OrderItem orderItemSave = orderItemRepository.save(orderItem);
			
			
			items.add(orderItemSave);
			
			
			
			
			Double priceItemQuantity = orderItem.getQuantity() * product.get().getPrice();
			totalPrice += priceItemQuantity;
		}
		
		orderSave.setTotalPrice(totalPrice);
		orderSave.setItems(items);
		
		orderRepository.save(orderSave);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(orderSave);
		
	}*/
	
	@PostMapping("/company/{idCompany}/representantive/{idRepresentantive}/client/{idClient}/orders")
    public ResponseEntity<?> create(
                        @PathVariable("idCompany") Long idCompany,
                        @PathVariable("idRepresentantive") Long idRepresentantive,
                        @PathVariable("idClient") Long idClient,
                        @RequestBody Order order) {
        // Verificando empresa
        Optional<Company> company = companyRepository.findById(idCompany);
        if(!company.isPresent()) {
            return ResponseEntity.status(400).build();
        }
        order.setCompany(company.get());
        order.setRepresentativeId(1L);
        order.setClientId(1L);
        order.setStatus("created");
        
        
        
        Double valorTotal = 0.0;
        Double valorDesconto = 0.0;
        for(OrderItem item : order.getItems()){
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            
            //definir mensagem de produto não encontrado
            if(p == null) return ResponseEntity.notFound().build();
            
            item.setPrice(p.getPrice());
            item.setDiscountPrice(p.getDiscontPrice());
            
            
            Double totalPriceItem = p.getTotalPrice() * item.getQuantity();
            Double totalDicount = p.getDiscontPrice() * item.getQuantity();
            
            
            item.setTotalPrice(totalPriceItem);
            item.setDiscountPrice(totalDicount);
            
            valorDesconto += item.getDiscountPrice();
            valorTotal += item.getTotalPrice();
            
        }
        order.setTotalPrice(valorTotal);
        
        Double discountPrice = order.getTotalPrice() * (order.getDicountId()/100);
        order.setDiscountPrice(discountPrice);

        order.setPriceToPay(order.getTotalPrice() - order.getDiscountPrice());

        Order orderSave = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderSave);
        
    }
	
	@PutMapping("/company/{idCompany}/representantive/{idRepresentantive}/client/{idClient}/orders/{idOrder}")
	public ResponseEntity<?> addItems(
							@PathVariable("idCompany") Long idCompany,
							@PathVariable("idRepresentantive") Long idRepresentantive,
	                        @PathVariable("idClient") Long idClient,
							@PathVariable("idOrder") Long idOrder,
				            @RequestBody OrderItem orderItem){
		// Verificando empresa
        Optional<Company> company = companyRepository.findById(idCompany);
        
        if(!company.isPresent()) {
            return ResponseEntity.status(400).build();
        }
        
        Order order = orderRepository.findById(idOrder).orElse(null);
        if(order == null) return ResponseEntity.notFound().build();
        
        
        orderItem.setOrder(order);
        List<OrderItem> itemsNew = new ArrayList<OrderItem>(order.getItems());
        itemsNew.add(orderItem);
        
        order.setItems(itemsNew);
        
        
        Double valorTotal = 0.0;
        Double valorDesconto = 0.0;
        for(OrderItem item : order.getItems()){
        	System.out.println("/");
        	
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            
            //definir mensagem de produto não encontrado
            if(p == null) return ResponseEntity.notFound().build();
            
            item.setPrice(p.getPrice());
            item.setDiscountPrice(p.getDiscontPrice());
            
            
            Double totalPriceItem = p.getTotalPrice() * item.getQuantity();
            Double totalDicount = p.getDiscontPrice() * item.getQuantity();
            
            
            item.setTotalPrice(totalPriceItem);
            item.setDiscountPrice(totalDicount);
            
            valorDesconto += item.getDiscountPrice();
            valorTotal += item.getTotalPrice();
            
        }
        System.out.println(" ");
        System.out.println(" ");
        order.setTotalPrice(valorTotal);
        
        Double discountPrice = order.getTotalPrice() * (order.getDicountId()/100);
        order.setDiscountPrice(discountPrice);

        order.setPriceToPay(order.getTotalPrice() - order.getDiscountPrice());

        Order orderSave = orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderSave);
	}
	
	@DeleteMapping("/company/{idCompany}/orders/{idOrder}/orders-items/{idOrderItem}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> removeItem(
			@PathVariable("idCompany") Long idCompany, 
			@PathVariable("idOrder") Long idOrder,
			@PathVariable("idOrderItem") Long idOrderItem) {
		Optional<Company> company = companyRepository.findById(idCompany);
		 
		if(!company.isPresent()) {
	        return ResponseEntity.status(400).build();
	    }
		
		Order order = orderRepository.findById(idOrder).orElse(null);
		if(order == null) return ResponseEntity.status(400).build();
		OrderItem orderItem = orderItemRepository.findById(idOrderItem).orElse(null);
		if(orderItem == null) return ResponseEntity.status(400).build();
		
		order.getItems().remove(orderItem);
		
		orderItemRepository.delete(orderItem);
		orderRepository.save(order);
		 
		
		return ResponseEntity.ok(null);
	}
	
	
	@DeleteMapping("/orders/{id}")
	public void deleta(@PathVariable Long id) {
		orderRepository.deleteById(id);
	}
	

}





