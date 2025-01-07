package ru.otus.java.professional.yampolskiy.hibernate.repositories;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.interfaces.Repository;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    private final Logger logger = LogManager.getLogger(this.getClass());
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
            rollbackTransaction(transaction);
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
            rollbackTransaction(transaction);
            throw e;
        }
    }

    @Override
    public void deleteById(ID id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
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
            rollbackTransaction(transaction);
            throw e;
        }
    }

    void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) {
            try {
                if (transaction.getStatus() != TransactionStatus.ROLLED_BACK) {
                    transaction.rollback();
                }
            } catch (Exception e) {
                logger.error("Ошибка при откате транзакции: ", e);
                System.err.println("Ошибка при откате транзакции: " + e.getMessage());
            }
        } else {
            logger.warn("Транзакция уже завершена или откатана.");
        }
    }
}
