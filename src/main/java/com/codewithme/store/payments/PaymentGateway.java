package com.codewithme.store.payments;

import com.codewithme.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
