package de.obi.demo.cart.dto;

public record ProductDto(Long id, String name, double totalPrice, int quantity) {
}
