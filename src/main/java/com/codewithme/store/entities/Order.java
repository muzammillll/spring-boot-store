package com.codewithme.store.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

   @Column(name = "created_at",insertable = false,updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "total_price")
   private BigDecimal totalPrice;

   @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST, CascadeType.REMOVE} )
   private Set<OrderItem> items = new LinkedHashSet<>();

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

   public static Order fromCart(Cart cart, User customer)
   {
       var order = new Order();
       order.setCustomer(customer);
       order.setStatus(OrderStatus.PENDING);
       order.setTotalPrice(cart.getTotalPrice());



       cart.getItems().forEach(item -> {
           var orderItem = new OrderItem(order,item.getProduct(),item.getQuantity());

           order.items.add(orderItem);
       });
       return order;
   }

   public boolean isPlacedBy(User customer)
   {
       return this.customer.equals(customer);
   }

}
