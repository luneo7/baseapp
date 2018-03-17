package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Product;
import br.com.lucas.baseapp.model.Store;
import br.com.lucas.baseapp.repository.ProductRepository;
import br.com.lucas.baseapp.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements GenericService<Product> {

    @Autowired
    ProductRepository productRepository;


    @Caching( evict = {
            @CacheEvict(value = "products", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="products", key="#product.productId")
        }
    )
    public Product put(Product product) {
        productRepository.save(product);
        return product;
    }

    @Cacheable(value="products")
    public Iterable<Product> getAll(){
        return productRepository.findAll();
    }

    public List<Product> findByDescription(String searchText) {
        return productRepository.findAllByDescriptionContaining(searchText);
    }

    public List<Product> findByName(String searchText) {
        return productRepository.findAllByProductNameContaining(searchText);
    }

    public List<Product> findByStore(Store store) {
        return productRepository.findAllByStore(store);
    }

    @Cacheable(value="products", key="#id")
    public Product getById(long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void remove(long id){
        productRepository.deleteById(id);
    }
}
