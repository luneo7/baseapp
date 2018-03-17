package br.com.lucas.baseapp.repository;

import br.com.lucas.baseapp.model.Product;
import br.com.lucas.baseapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDescriptionContaining(String searchTerm);
    List<Product> findAllByProductNameContaining(String searchTerm);
    List<Product> findAllByStore(Store store);
}
