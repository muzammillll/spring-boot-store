    package com.codewithme.store.services;

    import com.codewithme.store.dtos.OrderDto;
    import com.codewithme.store.exceptions.OrderNotFoundException;
    import com.codewithme.store.mappers.OrderMapper;
    import com.codewithme.store.repositories.OrderRepository;
    import lombok.AllArgsConstructor;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @AllArgsConstructor
    public class OrderService {
        private final AuthService authService;
        private final OrderRepository orderRepository;
        private final OrderMapper orderMapper;
        public List<OrderDto> getAllOrders()
        {
            var user = authService.getCurrentUser();
            var orders = orderRepository.getOrdersByCustomer(user);
            return orders.stream().map(orderMapper::toDto).toList();
        }

        public OrderDto getOrder(Long orderId) {
           var order = orderRepository
            .getOrderWithItems(orderId)
                   .orElseThrow( OrderNotFoundException::new);

           var user = authService.getCurrentUser();
           if(!order.isPlacedBy(user))
           {
               throw new AccessDeniedException("You dont have access to this order");
           }
            return orderMapper.toDto(order);
        }
    }
