package com.codewithme.store.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class OrderDto {

    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;

}
