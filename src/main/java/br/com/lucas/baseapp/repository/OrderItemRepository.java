package br.com.lucas.baseapp.repository;

import br.com.lucas.baseapp.model.Order;
import br.com.lucas.baseapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrder(Order order);
}
