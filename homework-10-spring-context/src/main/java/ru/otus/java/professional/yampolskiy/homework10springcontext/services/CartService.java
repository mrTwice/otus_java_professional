package ru.otus.java.professional.yampolskiy.homework10springcontext.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.homework10springcontext.components.Cart;
import ru.otus.java.professional.yampolskiy.homework10springcontext.entities.Product;
import ru.otus.java.professional.yampolskiy.homework10springcontext.repositories.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);
    private final ProductRepository productRepository;
    private final ObjectProvider<Cart> cartProvider;

    private final Map<Long, Cart> carts = new HashMap<>();

    public CartService(ProductRepository productRepository, ObjectProvider<Cart> cartProvider) {
        this.productRepository = productRepository;
        this.cartProvider = cartProvider;
    }

    public Cart getOrCreateCart(Long cartId) {
        return carts.computeIfAbsent(cartId, id -> {
            logger.info("Создана новая корзина с ID: {}", id);
            return cartProvider.getObject();
        });
    }

    public void addProductToCart(Long cartId, Long productId) {
        Cart cart = getOrCreateCart(cartId);
        Optional<Product> product = productRepository.getProductById(productId);
        if (product.isPresent()) {
            cart.addProduct(product.get());
            logger.info("Товар добавлен в корзину {}: {}", cartId, product.get());
        } else {
            logger.warn("Товар с ID {} не найден!", productId);
            System.out.println("Товар с таким ID не найден!");
        }
    }

    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = getOrCreateCart(cartId);
        Optional<Product> product = productRepository.getProductById(productId);
        if (product.isPresent()) {
            cart.removeProduct(product.get());
            logger.info("Товар удалён из корзины {}: {}", cartId, product.get());
        } else {
            logger.warn("Товар с ID {} не найден!", productId);
            System.out.println("Товар с таким ID не найден!");
        }
    }

    public void showCartContents(Long cartId) {
        Cart cart = getOrCreateCart(cartId);
        logger.info("Содержимое корзины {} запрошено", cartId);
        System.out.println("Содержимое корзины " + cartId + ":");
        for (Product product : cart.getItems()) {
            System.out.println(product);
        }
    }

    public Set<Long> getAllCarts() {
        return carts.keySet();
    }
}


