package ru.otus.java.professional.yampolskiy.patterns.two.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.professional.yampolskiy.patterns.two.configuration.DataSource;
import ru.otus.java.professional.yampolskiy.patterns.two.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemsDao implements Repository {
    private final Logger logger = LogManager.getLogger(ItemsDao.class);
    private static ItemsDao INSTANCE;
    private final DataSource dataSource;

    private ItemsDao() {
        this.dataSource = DataSource.getInstance();
    }

    public static ItemsDao getInstance() {
        if (INSTANCE == null) {
            synchronized (DataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemsDao();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        final String query = "SELECT * FROM items WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToItem(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении запроса {} для itemId={}: {}", query, itemId, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() {
        final String query = "SELECT * FROM items";
        List<Item> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                items.add(mapResultSetToItem(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении всех элементов: {}", e.getMessage(), e);
        }
        return items;
    }

    @Override
    public Optional<Item> add(Item newItem) {
        validateItem(newItem);
        final String query = "INSERT INTO items (title, price) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newItem.getTittle());
            statement.setDouble(2, newItem.getPrice());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newItem.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка добавления элемента: title={}, error={}", newItem.getTittle(), e.getMessage(), e);
            return Optional.empty();
        }
        return Optional.of(newItem);
    }

    @Override
    public Optional<Item> update(Item item) {
        validateItem(item);
        final String updateQuery = "UPDATE items SET title = ?, price = ? WHERE id = ?";
        final String selectQuery = "SELECT * FROM items WHERE id = ?";
        Item updatedItem = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            updateStatement.setString(1, item.getTittle());
            updateStatement.setDouble(2, item.getPrice());
            updateStatement.setLong(3, item.getId());
            updateStatement.executeUpdate();

            selectStatement.setLong(1, item.getId());
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    updatedItem = mapResultSetToItem(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка обновления элемента с id={}: {}", item.getId(), e.getMessage(), e);
        }

        return Optional.ofNullable(updatedItem);
    }

    @Override
    public Optional<Item> delete(Long itemId) {
        final String selectQuery = "SELECT * FROM items WHERE id = ?";
        final String deleteQuery = "DELETE FROM items WHERE id = ?";
        Item itemToDelete = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            selectStatement.setLong(1, itemId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    itemToDelete = mapResultSetToItem(resultSet);
                }
            }

            deleteStatement.setLong(1, itemId);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка удаления элемента с id={}: {}", itemId, e.getMessage(), e);
        }

        return Optional.ofNullable(itemToDelete);
    }

    public void deleteAll() {
        final String query = "DELETE FROM items";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int rowsDeleted = statement.executeUpdate();
            logger.info("Все записи удалены из таблицы items. Удалено строк: {}", rowsDeleted);
        } catch (SQLException e) {
            logger.error("Ошибка при удалении всех записей из таблицы items: ", e);
        }
    }

    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        double price = resultSet.getDouble("price");
        return new Item(id, title, price);
    }

    private void validateItem(Item item) {
        if (item.getTittle() == null || item.getTittle().isEmpty()) {
            throw new IllegalArgumentException("Title не может быть пустым");
        }
        if (item.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше 0");
        }
    }
}
