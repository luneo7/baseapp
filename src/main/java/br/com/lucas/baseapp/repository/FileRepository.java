package br.com.lucas.baseapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lucas.baseapp.model.File;


public interface FileRepository extends JpaRepository<File, Long> {
    File findFirstByFileName(String fileName);
}
