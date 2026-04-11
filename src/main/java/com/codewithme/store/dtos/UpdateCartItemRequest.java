package com.codewithme.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided")

    @Min(value = 1,message = "Quantity must be greater than zero.")
    @Max(value = 100,message = "Quantity must be less than or equal to 100.")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
