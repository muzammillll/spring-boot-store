package com.codewithme.store.payments;

import com.codewithme.store.entities.Order;
import com.codewithme.store.exceptions.CartEmptyException;
import com.codewithme.store.exceptions.CartNotFoundException;
import com.codewithme.store.repositories.CartRepository;
import com.codewithme.store.repositories.OrderRepository;
import com.codewithme.store.services.AuthService;
import com.codewithme.store.services.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final RazorpayPaymentGateway razorpayPaymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {

        var cart = cartRepository.getCartWithItems(request.getCartId())
                .orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            // ✅ FIX 1: Ensure total price is not null
            BigDecimal totalPrice = order.getTotalPrice();
            if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Invalid order amount");
            }

            // ✅ FIX 2: Convert to paise (IMPORTANT)
            int amount = totalPrice
                    .multiply(BigDecimal.valueOf(100))
                    .intValue();

            System.out.println("💰 Razorpay Amount (paise): " + amount);

            // ✅ FIX 3: Create Razorpay order
            String razorpayOrderId = razorpayPaymentGateway.createOrder(
                    amount,
                    "order_" + order.getId()
            );

            System.out.println("✅ Razorpay Order Created: " + razorpayOrderId);

            // ✅ Save Razorpay order id
            order.setRazorpayOrderId(razorpayOrderId);
            orderRepository.save(order);

            // 🧹 Clear cart AFTER success
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(
                    order.getId(),
                    razorpayOrderId
            );

        } catch (Exception e) {
            // 🔥 IMPORTANT DEBUG
            e.printStackTrace();

            // rollback order
            orderRepository.delete(order);

            throw new RuntimeException("Error creating checkout session", e);
        }
    }
}