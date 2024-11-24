package ru.otus.java.professional.yampolskiy.work.with.datadase.repositories;

import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryIdField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryTable;
import ru.otus.java.professional.yampolskiy.work.with.datadase.exceptions.ORMException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataSource dataSource;
    private final Map<String, String> sqlQueries = new HashMap<>();
    private Class<T> cls;
    private String tableName;
    private List<Field> cachedFields;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        validate(cls);
        this.dataSource = dataSource;
        this.prepareInsert(cls);
    }

    public void save(T entity) {
        String insert = sqlQueries.get("insert");
        if (insert == null) {
            throw new ORMException("SQL-запрос для вставки не найден");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(insert)) {

            for (int i = 0; i < cachedFields.size(); i++) {
                ps.setObject(i + 1, cachedFields.get(i).get(entity));
            }
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ORMException("Ошибка при сохранении объекта: " + entity);
        }
    }

    public List<T> findAll() {
        List<T> tEntities = new ArrayList<>();
        String findAll = sqlQueries.get("findAll");
        if (findAll == null) {
            throw new ORMException("SQL-запрос для получения списка не найден");
        }

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(findAll)) {

            while (resultSet.next()) {
                T entity = createEntityFromResultSet(resultSet);
                tEntities.add(entity);
            }

        } catch (Exception e) {
            throw new ORMException("Ошибка получения списка объектов.");
        }
        return tEntities;
    }


    public T findById(long id) {
        return null;
    }

    public void update(T entity) {

    }

    public void deleteById(Long id) {

    }

    private void prepareFindAll(Class<T> cls){
        String tableName = cls.getAnnotation(RepositoryTable.class).title();
        String sql = String.format("SELECT * FROM %s);", tableName);
        sqlQueries.put("findAll", sql);
    }

    private void prepareInsert(Class<T> cls) {
        String columnNames = cachedFields.stream()
                .map(f -> {
                    RepositoryField annotation = f.getAnnotation(RepositoryField.class);
                    return (annotation != null && !annotation.columnName().isEmpty())
                            ? annotation.columnName()
                            : f.getName();
                })
                .collect(Collectors.joining(", "));


        String placeholders = cachedFields.stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columnNames, placeholders);
        sqlQueries.put("insert", sql);
    }

    private T createEntityFromResultSet(ResultSet resultSet) {
        try {
            T entity = cls.getDeclaredConstructor().newInstance();
            for (Field field : cachedFields) {
                RepositoryField annotation = field.getAnnotation(RepositoryField.class);
                String columnName = (annotation != null && !annotation.columnName().isEmpty())
                        ? annotation.columnName()
                        : field.getName();

                Object value = resultSet.getObject(columnName);
                field.set(entity, value);
            }
            return entity;
        } catch (Exception e) {
            throw new ORMException("Ошибка создания объекта из ResultSet");
        }
    }


    private void validate(Class<T> cls) {
        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new ORMException("Класс не предназначен для создания репозитория, отсутствует аннотация @RepositoryTable");
        }
        this.cls = cls;
        this.tableName = cls.getAnnotation(RepositoryTable.class).title();

        this.cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(this::isRepositoryField)
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());
    }

    private boolean isRepositoryField(Field field) {
        return field.isAnnotationPresent(RepositoryField.class) &&
                !field.isAnnotationPresent(RepositoryIdField.class);
    }
}

