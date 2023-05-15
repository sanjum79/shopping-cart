package de.obi.demo.cart.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
    }

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(Throwable cause) {
        super(cause);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
