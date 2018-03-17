package br.com.lucas.baseapp.repository;

import br.com.lucas.baseapp.model.Cousine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CousineRepository extends JpaRepository<Cousine, Long> {
    List<Cousine> findAllByCousineNameContaining(String searchTerm);
}
