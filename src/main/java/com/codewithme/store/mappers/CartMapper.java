package com.codewithme.store.mappers;

import com.codewithme.store.dtos.CartDto;
import com.codewithme.store.dtos.CartItemDto;
import com.codewithme.store.entities.Cart;
import com.codewithme.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    //@Mapping(target = "items",source = "items")
    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);

}
