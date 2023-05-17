package de.obi.demo.cart.services;

import de.obi.demo.cart.dto.CartItemDto;
import de.obi.demo.cart.dto.ProductDto;
import de.obi.demo.cart.exception.NotFoundException;
import de.obi.demo.cart.exception.OutOfStockException;
import de.obi.demo.cart.model.Cart;
import de.obi.demo.cart.model.CartItem;
import de.obi.demo.cart.model.Product;
import de.obi.demo.cart.repositories.CartItemRepository;
import de.obi.demo.cart.repositories.CartRepository;
import de.obi.demo.cart.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

//    public ShoppingCartService() {
//    }

    public ShoppingCartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public void addItemToCart(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            return;
        }

        Cart shoppingCart = getCart(cartId);

        // Check if the quantity exceeds the item's inventory
        Product product = productRepository.getReferenceById(productId);
        long itemInventory = product.getQuantity();
        if (quantity > itemInventory) {
            throw new OutOfStockException("Item quantity exceeds inventory");
        }

        // Check if the item already exists in the cart
        CartItem existingItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(shoppingCart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            shoppingCart.addCartItem(newItem);
            cartItemRepository.save(newItem);
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void removeItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.getReferenceById(cartItemId);
        Product cartItemProduct = cartItem.getProduct();
        cartItemProduct.setQuantity(cartItem.getQuantity());
        productRepository.save(cartItemProduct);
        cartItemRepository.deleteById(cartItemId);
    }

    public void removeProductFromCart(Long productId) {
        CartItem cartItem = cartItemRepository.getCartItemByProduct(productId);
        if(cartItem == null) {
            throw new NotFoundException("There is no product with productId: "+ productId + " to delete!");
        }
        Product product = cartItem.getProduct();
        product.addToQuantity(cartItem.getQuantity());
        productRepository.save(product);
        cartItemRepository.deleteById(cartItem.getId());
    }

    public void removeAllItemsFromCart(Long cartId) {
        Cart cart = getCart(cartId);
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            throw new NotFoundException("There are no items in the cart.");
        }
        for (CartItem cartItem : cartItems) {
            Product cartItemProduct = cartItem.getProduct();
            cartItemProduct.addToQuantity(cartItem.getQuantity());
            productRepository.save(cartItemProduct);
            cartItemRepository.deleteById(cartItem.getId());
        }
        cart.setCartItems(new ArrayList<>());
    }

    public CartItemDto getCartItems(Long cartId) {
        Cart cart = getCart(cartId);
        List<CartItem> cartItems = cart.getCartItems();

        List<ProductDto> productDtos = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            double totalProductPrice = product.getPrice() * quantity;
            return new ProductDto(product.getId(), product.getName(), totalProductPrice, quantity);
        }).toList();

        double totalCartPrice = productDtos.stream()
                .mapToDouble(ProductDto::totalPrice)
                .sum();
        return new CartItemDto(cart.getId(), productDtos, totalCartPrice);
    }

    public double calculateCartTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        double total = 0.0;
        for (CartItem cartItem : cart.getCartItems()) {
            double itemTotal = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            total += itemTotal;
        }
        return total;
    }

    private Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
    }
}
