package de.obi.demo.cart.repositories;

import de.obi.demo.cart.model.CartItem;
import de.obi.demo.cart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.product.id = :productId")
    CartItem getCartItemByProduct(@Param("productId") Long productId);
}