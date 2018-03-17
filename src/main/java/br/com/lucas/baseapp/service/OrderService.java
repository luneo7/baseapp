package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Customer;
import br.com.lucas.baseapp.model.Order;
import br.com.lucas.baseapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements GenericService<Order> {

    @Autowired
    OrderRepository orderRepository;


    @Caching( evict = {
            @CacheEvict(value = "orders", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="orders", key="#order.orderId")
        }
    )
    public Order put(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Cacheable(value="orders")
    public Iterable<Order> getAll(){
        return orderRepository.findAll();
    }

    public List<Order> findByCustomer(Customer customer) {
        return orderRepository.findAllByCustomer(customer);
    }

    @Cacheable(value="orders", key="#id")
    public Order getById(long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @CacheEvict(value = "orders", allEntries = true)
    public void remove(long id){
        orderRepository.deleteById(id);
    }
}
