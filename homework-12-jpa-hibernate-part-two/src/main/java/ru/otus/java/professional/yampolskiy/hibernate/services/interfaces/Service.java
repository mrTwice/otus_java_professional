package ru.otus.java.professional.yampolskiy.hibernate.services.interfaces;

import java.util.List;

public interface Service<T, ID> {
    T save(T entity);

    T findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    T update(T entity);
}
