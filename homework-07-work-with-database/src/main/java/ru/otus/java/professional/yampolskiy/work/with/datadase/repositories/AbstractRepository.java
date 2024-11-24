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
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataSource dataSource;
    private final Map<String, String> sqlQueries = new HashMap<>();
    private Class<T> cls;
    private String tableName;
    private Field idField;
    private List<Field> cachedFields;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        validate(cls);
        this.dataSource = dataSource;
        this.prepareInsert();
        this.prepareFindAll();
        this.prepareFindById();
        this.prepareUpdate();
        this.prepareDeleteById();
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
        String findById = sqlQueries.get("findById");
        if (findById == null) {
            throw new ORMException("SQL-запрос для поиска по ID не найден");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(findById)) {

            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return createEntityFromResultSet(resultSet);
                } else {
                    return null;
                }
            }

        } catch (Exception e) {
            throw new ORMException("Ошибка поиска объекта по ID: " + id);
        }
    }

    public void update(T entity) {
        String updateQuery = sqlQueries.get("update");
        if (updateQuery == null) {
            throw new ORMException("SQL-запрос для обновления не найден");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuery)) {

            for (int i = 0; i < cachedFields.size(); i++) {
                Field field = cachedFields.get(i);
                field.setAccessible(true);
                ps.setObject(i + 1, field.get(entity));
            }

            Field idField = Arrays.stream(entity.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                    .findFirst()
                    .orElseThrow(() -> new ORMException("Не найдено поле с аннотацией @RepositoryIdField"));
            idField.setAccessible(true);
            ps.setObject(cachedFields.size() + 1, idField.get(entity));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new ORMException("Ошибка обновления объекта: " + entity);
        }
    }


    public void deleteById(Long id) {
        String deleteById = sqlQueries.get("deleteById");
        if (deleteById == null) {
            throw new ORMException("SQL-запрос для удаления по ID не найден");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteById)) {

            ps.setLong(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new ORMException("Удаление не выполнено. Объект с ID " + id + " не найден.");
            }

        } catch (Exception e) {
            throw new ORMException("Ошибка удаления объекта с ID: " + id);
        }
    }

    private void prepareDeleteById() {
        String idColumnName = idField.isAnnotationPresent(RepositoryField.class) &&
                !idField.getAnnotation(RepositoryField.class).columnName().isEmpty()
                ? idField.getAnnotation(RepositoryField.class).columnName()
                : idField.getName();

        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, idColumnName);
        sqlQueries.put("deleteById", sql);
    }

    private void prepareUpdate() {
        String idColumnName = getIdColumnName();

        String setClause = cachedFields.stream()
                .map(f -> {
                    RepositoryField annotation = f.getAnnotation(RepositoryField.class);
                    String columnName = (annotation != null && !annotation.columnName().isEmpty())
                            ? annotation.columnName()
                            : f.getName();
                    return columnName + " = ?";
                })
                .collect(Collectors.joining(", "));

        String sql = String.format("UPDATE %s SET %s WHERE %s = ?;", tableName, setClause, idColumnName);

        sqlQueries.put("update", sql);
    }


    private void prepareFindById() {
        String idColumnName = getIdColumnName();

        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, idColumnName);
        sqlQueries.put("findById", sql);
    }


    private void prepareFindAll() {
        String sql = String.format("SELECT * FROM %s;", tableName);
        sqlQueries.put("findAll", sql);
    }

    private void prepareInsert() {
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
                if (value != null) {
                    field.setAccessible(true);
                    field.set(entity, value);
                }
            }

            if (idField != null) {
                String idColumnName = idField.isAnnotationPresent(RepositoryField.class) &&
                        !idField.getAnnotation(RepositoryField.class).columnName().isEmpty()
                        ? idField.getAnnotation(RepositoryField.class).columnName()
                        : idField.getName();

                Object idValue = resultSet.getObject(idColumnName);
                if (idValue != null) {
                    idField.setAccessible(true);
                    idField.set(entity, idValue);
                }
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

        this.idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst()
                .orElseThrow(() -> new ORMException("Не найдено поле с аннотацией @RepositoryIdField"));
        this.idField.setAccessible(true);

        this.cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(this::isRepositoryField)
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());
    }

    private boolean isRepositoryField(Field field) {
        return field.isAnnotationPresent(RepositoryField.class) &&
                !field.isAnnotationPresent(RepositoryIdField.class);
    }

    private String getIdColumnName() {
        Field idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst()
                .orElseThrow(() -> new ORMException("Не найдено поле с аннотацией @RepositoryIdField"));

        return idField.isAnnotationPresent(RepositoryField.class) &&
                !idField.getAnnotation(RepositoryField.class).columnName().isEmpty()
                ? idField.getAnnotation(RepositoryField.class).columnName()
                : idField.getName();
    }

}

