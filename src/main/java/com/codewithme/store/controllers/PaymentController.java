package com.codewithme.store.controllers;

import com.codewithme.store.entities.Order;
import com.codewithme.store.entities.OrderStatus;
import com.codewithme.store.repositories.OrderRepository;
import com.codewithme.store.payments.RazorpayPaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final RazorpayPaymentGateway razorpayPaymentGateway;
    private final OrderRepository orderRepository;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature
    ) {

        try {
            // ✅ Step 1: Verify signature
            boolean isValid = razorpayPaymentGateway.verifyPayment(
                    razorpay_order_id,
                    razorpay_payment_id,
                    razorpay_signature
            );

            if (!isValid) {
                return ResponseEntity.badRequest().body("Payment Failed ❌ (Invalid Signature)");
            }

            // ✅ Step 2: Find order safely
            Order order = orderRepository
                    .findByRazorpayOrderId(razorpay_order_id)
                    .orElse(null);

            if (order == null) {
                return ResponseEntity.badRequest().body("Order not found ❌");
            }

            // ✅ Step 3: Update order status
            order.setStatus(OrderStatus.PAID);
            order.setRazorpayPaymentId(razorpay_payment_id);

            orderRepository.save(order);

            return ResponseEntity.ok("Payment Verified ✅");

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 Debugging

            return ResponseEntity
                    .internalServerError()
                    .body("Error verifying payment ❌");
        }
    }
}