package br.com.lucas.baseapp.service;

public interface GenericService<T> {
    T put(T entity);
    Iterable<T> getAll();
    T getById(long id);
    void remove(long id);
}
