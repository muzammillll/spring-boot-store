package com.codewithme.store.mappers;

import com.codewithme.store.dtos.OrderDto;
import com.codewithme.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
