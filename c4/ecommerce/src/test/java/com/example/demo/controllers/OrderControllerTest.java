package com.example.demo.controllers;

import com.example.demo.EcommerceTestUtil;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    String USER_NAME = "testUsername";
    String PASSWORD = "testPassword";
    String WRONG_USERNAME = "wrongUsername";

    @Before
    public void setUp(){
        orderController = new OrderController();
        EcommerceTestUtil.injectObjects(orderController, "orderRepository", orderRepository);
        EcommerceTestUtil.injectObjects(orderController, "userRepository", userRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal("23.34"));
        item.setDescription("itemDescription");
        List<Item> items = new ArrayList<>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername(USER_NAME);
        user.setPassword(PASSWORD);
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(new BigDecimal("23.34"));
        user.setCart(cart);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
    }

    @Test
    public void submitOrderSuccess() {
        ResponseEntity<UserOrder> response = orderController.submit(USER_NAME);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void submitOrderError() {
        ResponseEntity<UserOrder> response = orderController.submit(WRONG_USERNAME);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForUserSuccess() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser(USER_NAME);
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
    }

    @Test
    public void getOrdersForUserError() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser(WRONG_USERNAME);
        assertEquals(HttpStatus.NOT_FOUND, ordersForUser.getStatusCode());
    }
}

