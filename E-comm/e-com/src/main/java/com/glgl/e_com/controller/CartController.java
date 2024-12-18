package com.glgl.e_com.controller;

import com.glgl.e_com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

//    @PostMapping("/{userId}/add")
//    public ResponseEntity<String> addToCart(@PathVariable int userId,
//                                            @RequestParam int productId,
//                                            @RequestParam int quantity) {
//        try {
//            cartService.addToCart(userId, productId, quantity);
//            return ResponseEntity.ok("Product added to cart");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getCart(@PathVariable int userId) {
        return cartService.getCart(userId)
                .map(cart -> ResponseEntity.ok((Object) cart)) // Cast to Object for compatibility
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found"));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable int userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @DeleteMapping("/{userId}/remove")
//    public ResponseEntity<String> removeItem(@PathVariable int userId,
//                                             @RequestParam int productId) {
//        try {
//            cartService.removeItem(userId, productId);
//            return ResponseEntity.ok("Product removed from cart");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<?> calculateTotal(@PathVariable int userId) {
        try {
            double total = cartService.calculateTotal(userId);
            return ResponseEntity.ok("Total cart value: " + total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
