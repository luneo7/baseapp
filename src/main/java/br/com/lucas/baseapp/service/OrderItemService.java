package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Customer;
import br.com.lucas.baseapp.model.Order;
import br.com.lucas.baseapp.model.OrderItem;
import br.com.lucas.baseapp.repository.OrderItemRepository;
import br.com.lucas.baseapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements GenericService<OrderItem> {

    @Autowired
    OrderItemRepository orderItemRepository;


    @Caching( evict = {
            @CacheEvict(value = "orderitems", allEntries = true, beforeInvocation = true),
            @CacheEvict(value = "orders", allEntries = true)
        }, put = {
            @CachePut(value="orderitems", key="#orderItem.orderItemId")
        }
    )
    public OrderItem put(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    @Cacheable(value="orderitems")
    public Iterable<OrderItem> getAll(){
        return orderItemRepository.findAll();
    }

    public List<OrderItem> findByOrder(Order order) {
        return orderItemRepository.findAllByOrder(order);
    }

    @Cacheable(value="orderitems", key="#id")
    public OrderItem getById(long id){
        return orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderItem", "id", id));
    }

    @Caching( evict = {
            @CacheEvict(value = "orderitems", allEntries = true),
            @CacheEvict(value = "orders", allEntries = true)
        }
    )
    public void remove(long id){
        orderItemRepository.deleteById(id);
    }
}
