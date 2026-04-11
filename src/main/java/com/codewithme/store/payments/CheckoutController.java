package com.codewithme.store.payments;

import com.codewithme.store.dtos.ErrorDto;

import com.codewithme.store.exceptions.CartEmptyException;
import com.codewithme.store.exceptions.CartNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;


    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request)
    {
        try {
            return ResponseEntity.ok(checkoutService.checkout(request));
        }

        catch (Exception e)
        {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error creating a checkout session"));
        }

    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex)
    {

        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

}
