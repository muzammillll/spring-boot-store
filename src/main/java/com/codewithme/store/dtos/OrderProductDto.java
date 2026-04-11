package com.codewithme.store.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class OrderProductDto {
    private Long id;
    private String name;
    private BigDecimal price;


}
