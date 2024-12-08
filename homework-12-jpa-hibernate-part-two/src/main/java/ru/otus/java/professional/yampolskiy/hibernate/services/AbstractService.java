package ru.otus.java.professional.yampolskiy.hibernate.services;

import lombok.AllArgsConstructor;
import ru.otus.java.professional.yampolskiy.hibernate.exceptions.*;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.Repository;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.Service;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractService<T, ID> implements Service<T, ID> {
    protected final Repository<T, ID> repository;

    @Override
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new SaveException(entity.getClass().getSimpleName(), "Ошибка при сохранении объекта: " + entity, e);
        }
    }

    @Override
    public T findById(ID id) {
        try {
            T entity = repository.findById(id);
            if (entity == null) {
                throw new FindException(repository.getClass().getSimpleName(), "Объект с ID " + id + " не найден");
            }
            return entity;
        } catch (FindException e) {
            throw e;
        } catch (Exception e) {
            throw new FindException(repository.getClass().getSimpleName(), "Ошибка при поиске объекта с ID: " + id, e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ServiceException(repository.getClass().getSimpleName(), "Ошибка при получении всех объектов");
        }
    }

    @Override
    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteException(repository.getClass().getSimpleName(), "Ошибка при удалении объекта с ID: " + id, e);
        }
    }

    @Override
    public T update(T entity) {
        try {
            return repository.update(entity);
        } catch (Exception e) {
            throw new UpdateException(repository.getClass().getSimpleName(), "Ошибка при обновлении объекта: " + entity, e);
        }
    }
}

