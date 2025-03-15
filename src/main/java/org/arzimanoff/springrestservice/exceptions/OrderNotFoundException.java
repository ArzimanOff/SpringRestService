package org.arzimanoff.springrestservice.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Нет заказа с id = " + id);
    }
}
