package de.obi.demo.cart.controllers;

import de.obi.demo.cart.dto.CartItemDto;
import de.obi.demo.cart.model.Cart;
import de.obi.demo.cart.services.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = this.shoppingCartService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<String> addItemToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        if(quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity should be more than zero.");
        }
        shoppingCartService.addItemToCart(cartId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Item successfully added to cart.");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long productId) {
        shoppingCartService.removeProductFromCart(productId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> getCartItems(@PathVariable Long cartId) {
        CartItemDto cartItems = shoppingCartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<Double> calculateCartTotal(@PathVariable Long cartId) {
        double total = shoppingCartService.calculateCartTotalPrice(cartId);
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/{cartId}/empty")
    public ResponseEntity<String> emptyCart(@PathVariable Long cartId) {
        shoppingCartService.removeAllItemsFromCart(cartId);
        return ResponseEntity.ok("All items of cart are removed");
    }
}
