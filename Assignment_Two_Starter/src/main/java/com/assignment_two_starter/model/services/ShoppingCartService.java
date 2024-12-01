package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.CartItem;
import com.assignment_two_starter.model.entities.Customer;
import com.assignment_two_starter.model.entities.Product;
import com.assignment_two_starter.model.entities.ShoppingCart;
import com.assignment_two_starter.model.repositories.CartItemRepository;
import com.assignment_two_starter.model.repositories.ProductRepository;
import com.assignment_two_starter.model.repositories.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCart getOrCreateCart(Customer customer) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findByCustomer_UserId(customer.getUserId());

        if (cartOptional.isPresent()) {
            return cartOptional.get();
        } else {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setCustomer(customer);
            newCart.setCreatedAt(new Date());
            newCart.setUpdatedAt(new Date());
            return shoppingCartRepository.save(newCart);
        }
    }

    public ShoppingCart addProductToCart(Customer customer, Integer productId, Integer quantity) {
        ShoppingCart cart = getOrCreateCart(customer);

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            return cart;
        }

        Product product = productOptional.get();

        CartItem cartItem = cart.getCartItemList().stream()
                .filter(ci -> ci.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if(cartItem != null && cartItem.getQuantity() >= product.getStockQuantity()) {
            throw new IllegalArgumentException("Cart quantity exceeds stock quantity");
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setAddedAt(new Date());
            cart.getCartItemList().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cart.setUpdatedAt(new Date());
        shoppingCartRepository.save(cart);

        return cart;
    }

    public ShoppingCart updateCartItemQuantity(Integer cartItemId, Integer quantity) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()) {
            return null;
        }

        CartItem cartItem = cartItemOptional.get();

        if(quantity >= cartItem.getProduct().getStockQuantity()) {
            quantity = cartItem.getProduct().getStockQuantity();
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        ShoppingCart cart = cartItem.getCart();
        cart.setUpdatedAt(new Date());
        shoppingCartRepository.save(cart);

        return cart;
    }

    public ShoppingCart removeCartItem(Integer cartItemId) {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Cart item not found");
        }

        CartItem cartItem = cartItemOptional.get();
        ShoppingCart cart = cartItem.getCart();

        cart.getCartItemList().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cart.setUpdatedAt(new Date());
        shoppingCartRepository.save(cart);

        return cart;
    }

    public ShoppingCart clearCart(Customer customer) {
        ShoppingCart cart = getOrCreateCart(customer);

        cart.getCartItemList().clear();
        shoppingCartRepository.save(cart);

        cart.setUpdatedAt(new Date());
        return cart;
    }

    public ShoppingCart getCartById(Integer cartId) {
        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);

        if (cartOptional.isEmpty()) {
            throw new IllegalArgumentException("Cart not found");
        }

        return cartOptional.get();
    }

    public List<String> RemoveProductsFromCart( Product product ) {
        List<CartItem> cartItems = cartItemRepository.findByProduct(product);

        List<String> affectedUsers = cartItems.stream()
                .map(ci -> ci.getCart().getCustomer().getEmail())
                .distinct()
                .toList();

        cartItemRepository.deleteAll(cartItems);

        return affectedUsers;
    }
}
