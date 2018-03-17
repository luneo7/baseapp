package br.com.lucas.baseapp.service;

import br.com.lucas.baseapp.exception.ResourceNotFoundException;
import br.com.lucas.baseapp.model.File;
import br.com.lucas.baseapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;


    @Caching( evict = {
            @CacheEvict(value = "files", allEntries = true, beforeInvocation = true)
        }, put = {
            @CachePut(value="files", key="#file.fileId")
        }
    )
    public File save(File file) {
        fileRepository.save(file);
        return file;
    }

    @Cacheable(value="files")
    public Iterable<File> getAll(){
        return fileRepository.findAll();
    }

    @Cacheable(value="files", key="#id")
    public File get(long id){
        return fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File", "id", id));
    }

    @CacheEvict(value = "files", allEntries = true)
    public void remove(long id){
        fileRepository.deleteById(id);
    }
}
