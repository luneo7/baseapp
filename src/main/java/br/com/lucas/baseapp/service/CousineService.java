package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.Cousine;
import br.com.lucas.baseapp.model.File;
import br.com.lucas.baseapp.repository.CousineRepository;
import br.com.lucas.baseapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CousineService implements GenericService<Cousine> {

    @Autowired
    CousineRepository cousineRepository;


    @Caching( evict = {
            @CacheEvict(value = "cousines", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="cousines", key="#cousine.cousineId")
        }
    )
    public Cousine put(Cousine cousine) {
        cousineRepository.save(cousine);
        return cousine;
    }

    @Cacheable(value="cousines")
    public Iterable<Cousine> getAll(){
        return cousineRepository.findAll();
    }

    public List<Cousine> findByName(String searchText) {
        return cousineRepository.findAllByCousineNameContaining(searchText);
    }

    @Cacheable(value="cousines", key="#id")
    public Cousine getById(long id){
        return cousineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cousine", "id", id));
    }

    @CacheEvict(value = "cousines", allEntries = true)
    public void remove(long id){
        cousineRepository.deleteById(id);
    }
}
