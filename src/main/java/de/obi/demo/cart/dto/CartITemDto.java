package de.obi.demo.cart.dto;

import java.util.List;

public record CartITemDto(Long cartId, List<ProductDto> products, double totalPrice) {
}

