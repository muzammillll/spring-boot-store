package com.codewithme.store.exceptions;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException()
    {
        super("Cart is Empty");
    }
}
