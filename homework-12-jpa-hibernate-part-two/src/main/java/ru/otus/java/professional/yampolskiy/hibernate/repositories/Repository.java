package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import java.util.List;

public interface Repository<T, ID> {
    T save(T t);
    T findById(ID id);
    List<T> findAll();
    void delete(T t);
    void deleteById(ID id);
    T update(T t);

}
