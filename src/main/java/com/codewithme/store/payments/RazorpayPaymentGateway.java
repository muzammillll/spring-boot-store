package com.codewithme.store.payments;

import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@RequiredArgsConstructor
@Service
public class RazorpayPaymentGateway {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createOrder(int amount, String receiptId) throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", receiptId);

        var order = razorpayClient.orders.create(options);

        return order.get("id").toString();
    }

    public boolean verifyPayment(String orderId, String paymentId, String signature) {

        try {
            String payload = orderId + "|" + paymentId;

            String generatedSignature = hmacSHA256(payload, keySecret);

            return generatedSignature.equalsIgnoreCase(signature);

        } catch (Exception e) {
            throw new IllegalStateException("Payment verification failed", e);
        }
    }

    private String hmacSHA256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey =
                new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        mac.init(secretKey);

        byte[] hash = mac.doFinal(data.getBytes());

        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }
}