package com.codewithme.store.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException()
    {
        super("Order not found");
    }
}
