package ru.otus.java.professional.yampolskiy.homework10springcontext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.java.professional.yampolskiy.homework10springcontext.repositories.ProductRepository;
import ru.otus.java.professional.yampolskiy.homework10springcontext.services.CartService;

import java.util.Scanner;

@SpringBootApplication
public class Homework10SpringContextApplication implements CommandLineRunner {
	private static final Logger logger = LogManager.getLogger(Homework10SpringContextApplication.class);
	private final CartService cartService;
	private final ProductRepository productRepository;

	public Homework10SpringContextApplication(CartService cartService, ProductRepository productRepository) {
		this.cartService = cartService;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Homework10SpringContextApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.println("\n1. Показать список товаров");
				System.out.println("2. Создать новую корзину");
				System.out.println("3. Добавить товар в корзину");
				System.out.println("4. Удалить товар из корзины");
				System.out.println("5. Показать содержимое корзины");
				System.out.println("6. Показать список всех корзин");
				System.out.println("7. Выйти");
				System.out.print("Выберите действие: ");

				int choice = scanner.nextInt();

				switch (choice) {
					case 1:
						productRepository
								.getAllProducts()
								.forEach(System.out::println);
						logger.info("Список товаров отображён");
						break;

					case 2:
						System.out.print("Введите ID для новой корзины: ");
						long newCartId = scanner.nextLong();
						cartService.getOrCreateCart(newCartId);
						System.out.println("Корзина с ID " + newCartId + " успешно создана.");
						logger.info("Создана новая корзина с ID {}", newCartId);
						break;

					case 3:
						System.out.print("Введите ID корзины: ");
						long addCartId = scanner.nextLong();
						System.out.print("Введите ID товара для добавления: ");
						long addProductId = scanner.nextLong();
						cartService.addProductToCart(addCartId, addProductId);
						break;

					case 4:
						System.out.print("Введите ID корзины: ");
						long removeCartId = scanner.nextLong();
						System.out.print("Введите ID товара для удаления: ");
						long removeProductId = scanner.nextLong();
						cartService.removeProductFromCart(removeCartId, removeProductId);
						break;

					case 5:
						System.out.print("Введите ID корзины: ");
						long viewCartId = scanner.nextLong();
						cartService.showCartContents(viewCartId);
						break;

					case 6:
						System.out.println("Список всех корзин:");
						cartService.getAllCarts().forEach(cartId -> {
							System.out.println("Корзина ID: " + cartId);
						});
						logger.info("Список всех корзин отображён");
						break;

					case 7:
						logger.info("Приложение завершает работу");
						System.out.println("Выход из приложения...");
						System.exit(0);
						break;

					default:
						logger.warn("Выбрано неверное действие: {}", choice);
						System.out.println("Неверный выбор. Попробуйте снова.");
				}
			}
		}
	}

}

