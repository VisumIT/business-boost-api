package com.visumIT.Business.boost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.visumIT.Business.boost.models.Order;
import com.visumIT.Business.boost.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

	/*Iterable<OrderItem> findByOrder(Order order);
	OrderItem findByCodigo(long id);*/
}
