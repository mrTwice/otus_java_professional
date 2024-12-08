package ru.otus.java.professional.yampolskiy.hibernate;

import ru.otus.java.professional.yampolskiy.hibernate.configurations.HibernateConfiguration;
import ru.otus.java.professional.yampolskiy.hibernate.entities.*;
import ru.otus.java.professional.yampolskiy.hibernate.repositories.*;
import ru.otus.java.professional.yampolskiy.hibernate.services.*;
import ru.otus.java.professional.yampolskiy.hibernate.services.interfaces.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final ClientService clientService = new ClientServiceImpl(new ClientRepositoryImpl(HibernateConfiguration.getSessionFactory()));
    public static final OrderService orderService = new OrderServiceImpl(new OrderRepositoryImpl(HibernateConfiguration.getSessionFactory()));
    public static final PriceHistoryService priceHistoryService = new PriceHistoryServiceImpl(new PriceHistoryRepository(HibernateConfiguration.getSessionFactory()));
    public static final ProductService productService = new ProductServiceImpl(new ProductRepositoryImpl(HibernateConfiguration.getSessionFactory()));

    public static void main(String[] args) {

        createClient("User1");
        createClient("User2");
        createClient("User3");
        createClient("User4");

        createProduct("Milk", new BigDecimal("10.99"));
        createProduct("Bread", new BigDecimal("5.99"));
        createProduct("Cheese", new BigDecimal("9.99"));
        createProduct("Butter", new BigDecimal("7.49"));
        createProduct("Eggs", new BigDecimal("4.59"));
        createProduct("Apple Juice", new BigDecimal("3.99"));
        createProduct("Orange Juice", new BigDecimal("4.49"));
        createProduct("Chicken Breast", new BigDecimal("8.99"));
        createProduct("Pasta", new BigDecimal("2.99"));
        createProduct("Rice", new BigDecimal("1.99"));
        createProduct("Tomato Sauce", new BigDecimal("2.49"));
        createProduct("Potatoes", new BigDecimal("3.29"));
        createProduct("Carrots", new BigDecimal("2.19"));
        createProduct("Apples", new BigDecimal("3.49"));
        createProduct("Bananas", new BigDecimal("1.99"));
        createProduct("Strawberries", new BigDecimal("5.99"));
        createProduct("Blueberries", new BigDecimal("6.49"));
        createProduct("Greek Yogurt", new BigDecimal("4.99"));
        createProduct("Milk Chocolate", new BigDecimal("2.49"));
        createProduct("Dark Chocolate", new BigDecimal("2.99"));
        createProduct("Coffee Beans", new BigDecimal("12.99"));
        createProduct("Green Tea", new BigDecimal("7.99"));
        createProduct("Peanut Butter", new BigDecimal("5.49"));
        createProduct("Honey", new BigDecimal("8.99"));


        System.out.println(clientService.findAll());
        System.out.println(productService.findAll());

        Product product = productService.findByName("Milk");
        Product product2 = productService.findByName("Pasta");
        updateProductPrice(product.getId(), new BigDecimal("99.99"));

        System.out.println(productService.findPriceHistoryById(product.getId()));

        Client client1 = clientService.findByLogin("User1");
        Client client2 = clientService.findByLogin("User2");
        Client client3 = clientService.findByLogin("User3");
        Client client4 = clientService.findByLogin("User4");

        Map<Long, Integer> orderItems1 = new HashMap<>();
        Map<Long, Integer> orderItems2 = new HashMap<>();
        Map<Long, Integer> orderItems3 = new HashMap<>();
        Map<Long, Integer> orderItems4 = new HashMap<>();
        orderItems1.put(productService.findByName("Honey").getId(), 1);
        orderItems1.put(productService.findByName("Coffee Beans").getId(), 2);
        orderItems1.put(productService.findByName("Milk Chocolate").getId(), 3);

        orderItems2.put(productService.findByName("Tomato Sauce").getId(), 1);
        orderItems2.put(productService.findByName("Milk").getId(), 2);
        orderItems2.put(productService.findByName("Butter").getId(), 3);

        orderItems3.put(productService.findByName("Strawberries").getId(), 1);
        orderItems3.put(productService.findByName("Blueberries").getId(), 2);
        orderItems3.put(productService.findByName("Pasta").getId(), 3);

        orderItems4.put(productService.findByName("Pasta").getId(), 10);

        orderService.saveOrderWithItems(client1.getId(), orderItems1);
        orderService.saveOrderWithItems(client2.getId(), orderItems2);
        orderService.saveOrderWithItems(client3.getId(), orderItems3);
        orderService.saveOrderWithItems(client4.getId(), orderItems4);

        System.out.println(clientService.getOrderingProductsByClientId(client1.getId()));
        System.out.println(clientService.getOrderingProductsByClientId(client2.getId()));
        System.out.println(clientService.getOrderingProductsByClientId(client3.getId()));
        System.out.println(clientService.getOrderingProductsByClientId(client4.getId()));


        List<Client> clients = productService.findClientsByProductId(product2.getId());
        System.out.println(clients);

    }

    public static void createClient(String login) {
        Client client = new Client();
        client.setLogin(login);
        clientService.save(client);
    }

    public static void createProduct(String title, BigDecimal price) {
        Product product = new Product();
        product.setTitle(title);
        product.setCurrentPrice(price);
        productService.save(product);
    }

    public static void updateProductPrice(Long productId, BigDecimal newPrice) {
        Product product = productService.findById(productId);
        if (product != null) {
            PriceHistory history = new PriceHistory();
            history.setProduct(product);
            history.setPrice(product.getCurrentPrice());
            history.setCreatedAt(LocalDateTime.now());
            priceHistoryService.save(history);

            product.setCurrentPrice(newPrice);
            productService.update(product);
        }
    }
}
