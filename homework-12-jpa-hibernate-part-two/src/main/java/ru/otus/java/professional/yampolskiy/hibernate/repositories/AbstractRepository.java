package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

@AllArgsConstructor
public class AbstractRepository<T, ID> implements Repository<T, ID> {
    private final Class<T> entityClass;
    protected SessionFactory sessionFactory;

    @Override
    public T save(T entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public T findById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        }
    }

    @Override
    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void deleteById(ID id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(findById(id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public T update(T t) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(t);
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
