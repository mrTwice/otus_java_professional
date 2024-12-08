package ru.otus.java.professional.yampolskiy.hibernate.services;

import lombok.AllArgsConstructor;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.Repository;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractService<T, ID> implements Service<T, ID> {
    protected final Repository<T, ID> repository;

    @Override
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении объекта: " + entity, e);
        }
    }

    @Override
    public T findById(ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске объекта по ID: " + id, e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении всех объектов", e);
        }
    }

    @Override
    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении объекта по ID: " + id, e);
        }
    }

    @Override
    public T update(T entity) {
        try {
            return repository.update(entity);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении объекта: " + entity, e);
        }
    }
}

