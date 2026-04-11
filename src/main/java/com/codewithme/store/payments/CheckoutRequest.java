package com.codewithme.store.payments;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CheckoutRequest {

    @NotNull(message = "Cart id is required")
    private UUID cartId;

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }
}
