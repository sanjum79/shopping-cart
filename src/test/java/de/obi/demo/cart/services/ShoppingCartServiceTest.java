package de.obi.demo.cart.services;

import de.obi.demo.cart.dto.CartItemDto;
import de.obi.demo.cart.dto.ProductDto;
import de.obi.demo.cart.exception.OutOfStockException;
import de.obi.demo.cart.model.Cart;
import de.obi.demo.cart.model.CartItem;
import de.obi.demo.cart.model.Product;
import de.obi.demo.cart.repositories.CartItemRepository;
import de.obi.demo.cart.repositories.CartRepository;
import de.obi.demo.cart.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    public void shouldAddItemToEmptyCart() {
        Long cartId = 1L;
        Long productId = 1L;
        Cart cart = new Cart(cartId, new ArrayList<>());
        Product product = new Product(productId, "product-1", 50.0, 5);

        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));
        when(productRepository.getReferenceById(productId))
                .thenReturn(product);

        shoppingCartService.addItemToCart(cartId, productId, 2);
        assertThat("", !cart.getCartItems().isEmpty());
        assertThat("", product.getQuantity().equals(3));
    }

    @Test
    public void shouldIncreaseQuantityOfExistingCartItem() {
        // Given
        Long cartId = 1L;
        Long productId = 1L;
        Cart cart = new Cart(cartId, new ArrayList<>());
        Product product = new Product(productId, "product-1", 50.0, 5);
        CartItem cartItem = new CartItem(1L, cart, product, 2);
        cart.setCartItems(List.of(cartItem));

        // When
        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));
        when(productRepository.getReferenceById(productId))
                .thenReturn(product);

        // Then
        shoppingCartService.addItemToCart(cartId, productId, 2);
        assertThat("", !cart.getCartItems().isEmpty());
        assertThat("", product.getQuantity().equals(3));
        assertThat("", cartItem.getQuantity() == 4);
    }

    @Test
    public void shouldThrowOutOfStockException() {
        // Given
        Long cartId = 1L;
        Long productId = 1L;
        Cart cart = new Cart(cartId, new ArrayList<>());
        Product product = new Product(productId, "product-1", 50.0, 5);

        // When
        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));
        when(productRepository.getReferenceById(productId))
                .thenReturn(product);

        // Then
        Exception exception = Assertions.assertThrows(OutOfStockException.class,
                () -> shoppingCartService.addItemToCart(cartId, productId, 6));

        assertThat("Exception Message", exception.getMessage().equals("Item quantity exceeds inventory"));
    }

    @Test
    public void shouldRemoveItemFromCart() {
        // Given
        Long cartId = 1L;
        Long productId = 1L;
        Long cartItemId = 1L;
        Cart cart = new Cart(cartId, new ArrayList<>());
        Product product = new Product(productId, "product-1", 50.0, 0);
        CartItem cartItem = new CartItem(cartItemId, cart, product, 5);
        cart.setCartItems(List.of(cartItem));

        // When
        when(cartItemRepository.getReferenceById(cartItemId))
                .thenReturn(cartItem);

        // Then
        assertThat("product quantity before removal", product.getQuantity() == 0);
        shoppingCartService.removeItemFromCart(cartItemId);
        assertThat("product quantity after removal", product.getQuantity() == 5);
    }

    @Test
    public void shouldReturnWholeCartContents() {
        // Given
        Long cartId = 1L;
        Cart cart = new Cart(cartId, new ArrayList<>());
        Product product1 = new Product(1L, "product-1", 50.0, 5);
        Product product2 = new Product(2L, "product-2", 40.0, 5);
        Product product3 = new Product(3L, "product-3", 30.0, 5);
        CartItem cartItem1 = new CartItem(1L, cart, product1, 2);
        CartItem cartItem2 = new CartItem(2L, cart, product2, 2);
        CartItem cartItem3 = new CartItem(3L, cart, product3, 2);
        cart.setCartItems(List.of(cartItem1, cartItem2, cartItem3));

        // When
        when(cartRepository.findById(cartId))
                .thenReturn(Optional.of(cart));

        // Then
        CartItemDto dto = shoppingCartService.getCartItems(cartId);
        List<ProductDto> productDtos = dto.products();
        assertThat("Total cartItems", productDtos.size() == 3);
        productDtos.forEach(productDto -> {
            if (productDto.id().equals(1L)) {
                assertThat("Total price of first product", productDto.totalPrice() == 100.0);
                assertThat("Quantity of first product", productDto.quantity() == 2);
            } else if (productDto.id().equals(2L)) {
                assertThat("Total price of second product", productDto.totalPrice() == 80.0);
                assertThat("Quantity of second product", productDto.quantity() == 2);
            } else {
                assertThat("Total price of third product", productDto.totalPrice() == 60.0);
                assertThat("Quantity of third product", productDto.quantity() == 2);
            }
        });
        assertThat("Total price of the cart", dto.totalPrice() == 240.00);
    }
}
