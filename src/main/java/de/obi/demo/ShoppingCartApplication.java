package de.obi.demo;

import de.obi.demo.cart.model.Cart;
import de.obi.demo.cart.model.Product;
import de.obi.demo.cart.repositories.CartRepository;
import de.obi.demo.cart.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProductRepository productRepository, CartRepository cartRepository) {
		return args -> {
			productRepository.save(new Product(1L, "Test Article 1", 50.00, 5));
			productRepository.save(new Product(2L, "Test Article 2", 55.00, 5));
			productRepository.save(new Product(3L, "Test Article 3", 60.00, 5));
			productRepository.save(new Product(4L, "Test Article 4", 70.00, 5));
			productRepository.save(new Product(5L, "Test Article 5", 56.99, 5));
			productRepository.save(new Product(6L, "Test Article 6", 29.99, 5));
			productRepository.save(new Product(7L, "Test Article 7", 49.90, 5));

			cartRepository.save(new Cart(1L, new ArrayList<>()));
		};
	}

}
