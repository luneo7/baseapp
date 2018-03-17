package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Cousine;
import br.com.lucas.baseapp.model.Customer;
import br.com.lucas.baseapp.repository.CousineRepository;
import br.com.lucas.baseapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements GenericService<Customer> {

    @Autowired
    CustomerRepository customerRepository;


    @Caching( evict = {
            @CacheEvict(value = "customers", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="customers", key="#customer.customerId")
        }
    )
    public Customer put(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @Cacheable(value="customers")
    public Iterable<Customer> getAll(){
        return customerRepository.findAll();
    }

    @Cacheable(value="customers", key="#id")
    public Customer getById(long id){
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }

    public Customer getByEmail(String email){
        return customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    }

    @CacheEvict(value = "customers", allEntries = true)
    public void remove(long id){
        customerRepository.deleteById(id);
    }
}
