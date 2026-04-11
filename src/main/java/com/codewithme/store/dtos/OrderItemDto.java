package com.codewithme.store.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class OrderItemDto {
    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;

}
