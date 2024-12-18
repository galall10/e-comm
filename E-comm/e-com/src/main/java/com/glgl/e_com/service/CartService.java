package com.glgl.e_com.service;

import com.glgl.e_com.model.Cart;
import com.glgl.e_com.model.Product;
import com.glgl.e_com.model.User;
import com.glgl.e_com.reop.CartRepo;
import com.glgl.e_com.reop.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepo repo;

    @Autowired
    private ProductService productService;

    public Cart getOrCreateCart(int userId) {
        return repo.findByUserId(userId).orElseGet(() -> createCart(userId));
    }

    public Optional<Cart> getCart(int userId) {
        return repo.findByUserId(userId);
    }

    @Autowired
    private UserRepo userRepository; // Repository for User entity

    public Cart createCart(int userId) {
        // Fetch the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and set up the Cart
        Cart newCart = new Cart();
        newCart.setUser(user); // Set the User object
        newCart.setDate(new Date());

        return repo.save(newCart);
    }

//    public void addToCart(int userId, int productId, int quantity) throws Exception {
//        if (quantity <= 0) {
//            throw new IllegalArgumentException("Quantity must be greater than zero");
//        }
//
//        Cart cart = getOrCreateCart(userId);
//        Product product = productService.getProduct(productId)
//                .orElseThrow(() -> new Exception("Product not found"));
//
//        cart.addItem(product, quantity);
//        repo.save(cart);
//    }
//
//    public void removeItem(int userId, int productId) throws Exception {
//        Cart cart = getCart(userId)
//                .orElseThrow(() -> new Exception("Cart not found"));
//
//        Product product = productService.getProduct(productId)
//                .orElseThrow(() -> new Exception("Product not found"));
//
//        cart.removeItem(product);
//        repo.save(cart);
//    }

    public void clearCart(int userId) throws Exception {
        Cart cart = getCart(userId)
                .orElseThrow(() -> new Exception("Cart not found"));
        cart.clearItems();
        repo.save(cart);
    }

    public double calculateTotal(int userId) throws Exception {
        Cart cart = getCart(userId)
                .orElseThrow(() -> new Exception("Cart not found"));
        return cart.getTotalPrice();
    }
}
