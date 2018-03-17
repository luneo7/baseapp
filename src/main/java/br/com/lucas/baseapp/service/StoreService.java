package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Cousine;
import br.com.lucas.baseapp.model.Store;
import br.com.lucas.baseapp.repository.CousineRepository;
import br.com.lucas.baseapp.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService implements GenericService<Store> {

    @Autowired
    StoreRepository storeRepository;


    @Caching( evict = {
            @CacheEvict(value = "stores", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="stores", key="#store.storeId")
        }
    )
    public Store put(Store store) {
        storeRepository.save(store);
        return store;
    }

    @Cacheable(value="stores")
    public Iterable<Store> getAll(){
        return storeRepository.findAll();
    }

    public List<Store> findByAddress(String searchText) {
        return storeRepository.findAllByStoreAddressIsContaining(searchText);
    }

    public List<Store> findByCousine(Cousine cousine) {
        return storeRepository.findAllByCousine(cousine);
    }

    @Cacheable(value="stores", key="#id")
    public Store getById(long id){
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
    }

    @CacheEvict(value = "stores", allEntries = true)
    public void remove(long id){
        storeRepository.deleteById(id);
    }
}
