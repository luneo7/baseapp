package br.com.lucas.baseapp.repository;

import br.com.lucas.baseapp.model.Cousine;
import br.com.lucas.baseapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreAddressIsContaining(String searchTerm);
    List<Store> findAllByCousine(Cousine cousine);
}
