package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.*;
import com.assignment_two_starter.model.repositories.OrderItemsRepository;
import com.assignment_two_starter.model.repositories.OrdersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    private final OrderItemsRepository orderItemsRepository;

    private final ShoppingCartService shoppingCartService;

    private final AddressService addressService;

    private final ProductService productService;

    enum OrderStatus {
        pending,
        processing,
        shipped,
        delivered,
        cancelled,
    }

    public OrdersService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, ShoppingCartService shoppingCartService, AddressService addressService, ProductService productService) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.shoppingCartService = shoppingCartService;
        this.addressService = addressService;
        this.productService = productService;
    }

    public Orders createOrder(Integer cartId, Integer addressId) {
        ShoppingCart cart = shoppingCartService.getCartById(cartId);

        Orders order = new Orders();
        BigDecimal orderTotal = BigDecimal.ZERO;
        order.setCustomer(cart.getCustomer());

        Address address = addressService.getAddressById(addressId);
        order.setShippingAddressId(address);

        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.pending.toString());
        order.setAddressChangeFee(0.00);

        Date estimatedShippingDate = new Date();
        estimatedShippingDate.setTime(estimatedShippingDate.getTime() + 10 * 24 * 60 * 60 * 1000);
        order.setEstimatedShippingDate(estimatedShippingDate);
        order.setTotalAmount(BigDecimal.ZERO);

        ordersRepository.save(order);

        for(CartItem cartItem : cart.getCartItemList()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(cartItem.getProduct().getPrice()));

            Double itemTotal = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            orderItem.setTotalPrice(itemTotal);

            orderTotal = orderTotal.add(BigDecimal.valueOf(itemTotal));

            orderItemsRepository.save(orderItem);

            productService.updateProductQuantity(cartItem.getProduct().getProductId(), cartItem.getQuantity());
        }

        order.setTotalAmount(orderTotal);

        ordersRepository.save(order);

        shoppingCartService.clearCart(cart.getCustomer());



        return order;
    }

    public List<Orders> getOrdersByCustomerId(Integer customerId) {
        Optional<List<Orders>> orders = ordersRepository.findByCustomer_UserId(customerId);

        if (orders.isPresent()) {
            return orders.get();
        } else {
            return null;
        }
    }

    public Orders getOrderById(Integer orderId) {
        Optional<Orders> order = ordersRepository.findById(orderId);

        if (order.isPresent()) {
            return order.get();
        } else {
            return null;
        }
    }
}
