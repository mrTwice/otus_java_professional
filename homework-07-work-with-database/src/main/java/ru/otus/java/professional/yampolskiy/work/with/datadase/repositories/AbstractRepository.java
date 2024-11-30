package ru.otus.java.professional.yampolskiy.work.with.datadase.repositories;

import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryIdField;
import ru.otus.java.professional.yampolskiy.work.with.datadase.annotations.RepositoryTable;
import ru.otus.java.professional.yampolskiy.work.with.datadase.configurations.DataSource;
import ru.otus.java.professional.yampolskiy.work.with.datadase.exceptions.ORMException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataSource dataSource;
    private final Map<String, String> sqlQueries = new HashMap<>();
    private final Class<T> cls;
    private final String tableName;
    private final Field idField;
    private final List<Field> cachedFields;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        validate(cls);
        this.dataSource = dataSource;
        this.cls = cls;
        this.tableName = cls.getAnnotation(RepositoryTable.class).title();
        this.idField = getIdField(cls);
        this.cachedFields = getRepositoryFields(cls);
    }

    public void save(T entity) {
        String insertQuery = getOrPrepareQuery("insert", this::generateInsertQuery);
        executeUpdate(insertQuery, ps -> {
            for (int i = 0; i < cachedFields.size(); i++) {
                ps.setObject(i + 1, cachedFields.get(i).get(entity));
            }
        });
    }

    public List<T> findAll() {
        String findAllQuery = getOrPrepareQuery("findAll", this::generateFindAllQuery);
        return executeQuery(findAllQuery, ps -> {}, this::createEntityFromResultSet);
    }

    public T findById(long id) {
        String findByIdQuery = getOrPrepareQuery("findById", this::generateFindByIdQuery);
        return executeQuery(findByIdQuery, ps -> ps.setLong(1, id), this::createEntityFromResultSet).stream()
                .findFirst()
                .orElse(null);
    }

    public void update(T entity) {
        String updateQuery = getOrPrepareQuery("update", this::generateUpdateQuery);
        executeUpdate(updateQuery, ps -> {
            for (int i = 0; i < cachedFields.size(); i++) {
                ps.setObject(i + 1, cachedFields.get(i).get(entity));
            }
            ps.setObject(cachedFields.size() + 1, idField.get(entity));
        });
    }

    public void deleteById(Long id) {
        String deleteQuery = getOrPrepareQuery("deleteById", this::generateDeleteQuery);
        executeUpdate(deleteQuery, ps -> ps.setLong(1, id));
    }


    private String getOrPrepareQuery(String key, Supplier<String> queryGenerator) {
        return sqlQueries.computeIfAbsent(key, k -> queryGenerator.get());
    }

    private void executeUpdate(String sql, SQLConsumer<PreparedStatement> preparer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            preparer.accept(ps);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ORMException("Ошибка выполнения запроса: " + sql, e);
        }
    }

    private <R> List<R> executeQuery(String sql, SQLConsumer<PreparedStatement> preparer, SQLFunction<ResultSet, R> mapper) {
        List<R> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            preparer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.apply(rs));
                }
            }
        } catch (Exception e) {
            throw new ORMException("Ошибка выполнения запроса: " + sql, e);
        }
        return results;
    }

    private T createEntityFromResultSet(ResultSet resultSet) throws Exception {
        T entity = cls.getDeclaredConstructor().newInstance();
        for (Field field : cachedFields) {
            String columnName = getColumnName(field);
            Object value = resultSet.getObject(columnName);
            if (value != null) {
                field.set(entity, value);
            }
        }
        if (idField != null) {
            String idColumnName = getColumnName(idField);
            Object idValue = resultSet.getObject(idColumnName);
            if (idValue != null) {
                idField.set(entity, idValue);
            }
        }
        return entity;
    }

    private String getColumnName(Field field) {
        RepositoryField annotation = field.getAnnotation(RepositoryField.class);
        return (annotation != null && !annotation.columnName().isEmpty()) ? annotation.columnName() : field.getName();
    }

    private Field getIdField(Class<T> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .peek(f -> f.setAccessible(true))
                .findFirst()
                .orElseThrow(() -> new ORMException("Не найдено поле с аннотацией @RepositoryIdField"));
    }

    private List<Field> getRepositoryFields(Class<T> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class) &&
                        !f.isAnnotationPresent(RepositoryIdField.class))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());
    }

    private void validate(Class<T> cls) {
        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new ORMException("Класс должен быть аннотирован @RepositoryTable");
        }
    }

    private String generateInsertQuery() {
        String columns = getColumnNames(cachedFields);
        String placeholders = "?".repeat(cachedFields.size()).replace("", ", ").trim();
        return "INSERT INTO %s (%s) VALUES (%s);".formatted(tableName, columns, placeholders);
    }

    private String getColumnNames(List<Field> fields) {
        return fields.stream()
                .map(this::getColumnName)
                .collect(Collectors.joining(", "));
    }

    private String generateFindAllQuery() {
        return String.format("SELECT * FROM %s;", tableName);
    }

    private String generateFindByIdQuery() {
        String idColumn = getColumnName(idField);
        return String.format("SELECT * FROM %s WHERE %s = ?;", tableName, idColumn);
    }

    private String generateUpdateQuery() {
        String setClause = cachedFields.stream()
                .map(f -> getColumnName(f) + " = ?")
                .collect(Collectors.joining(", "));
        String idColumn = getColumnName(idField);
        return String.format("UPDATE %s SET %s WHERE %s = ?;", tableName, setClause, idColumn);
    }

    private String generateDeleteQuery() {
        String idColumn = getColumnName(idField);
        return String.format("DELETE FROM %s WHERE %s = ?;", tableName, idColumn);
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    private interface SQLFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
