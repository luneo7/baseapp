package br.com.lucas.baseapp.repository;

import br.com.lucas.baseapp.model.Customer;
import br.com.lucas.baseapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(Customer customer);
}
