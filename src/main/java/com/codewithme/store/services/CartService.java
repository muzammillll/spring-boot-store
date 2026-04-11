package com.codewithme.store.services;

import com.codewithme.store.dtos.CartDto;
import com.codewithme.store.dtos.CartItemDto;
import com.codewithme.store.entities.Cart;
import com.codewithme.store.exceptions.CartNotFoundException;
import com.codewithme.store.exceptions.ProductNotFoundException;
import com.codewithme.store.mappers.CartMapper;
import com.codewithme.store.repositories.CartRepository;
import com.codewithme.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;




    public CartDto createCart()
    {
        var cart = new Cart();
        cartRepository.save(cart);

 return  cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId,Long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null)
        {
           throw new CartNotFoundException();
        }
        var product = productRepository.findById(productId).orElse(null);

        if(product==null)
        {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null)
        {
           throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null)
        {
           throw new CartNotFoundException();
        }

        var cartItem =  cart.getItem(productId);
        if (cartItem == null)
        {
           throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart==null)
        {

           throw new CartNotFoundException();
        }

        cart.removeItem(productId);

        cartRepository.save(cart);


    }

    public void clearCart(UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
        {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }
}
