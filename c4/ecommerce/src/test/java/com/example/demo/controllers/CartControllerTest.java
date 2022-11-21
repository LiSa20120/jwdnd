package com.example.demo.controllers;

import com.example.demo.EcommerceTestUtil;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    String USER_NAME = "testUsername";
    String PASSWORD = "testPassword";
    String WRONG_USERNAME = "wrongUsername";

    @Before
    public void setUp() {
        cartController = new CartController();
        EcommerceTestUtil.injectObjects(cartController, "userRepository", userRepository);
        EcommerceTestUtil.injectObjects(cartController, "cartRepository", cartRepository);
        EcommerceTestUtil.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername(USER_NAME);
        user.setPassword(PASSWORD);
        user.setCart(cart);
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal("23.34"));
        item.setDescription("itemDescription");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void addToCartSuccess() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(USER_NAME);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart cart = response.getBody();
        assert cart != null;
        assertEquals(new BigDecimal("23.34"), cart.getTotal());
    }

    @Test
    public void addToCartError() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(WRONG_USERNAME);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromCartSuccess() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(USER_NAME);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(USER_NAME);
        response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart cart = response.getBody();
        assert cart != null;
        assertEquals(0, cart.getTotal().intValue());
    }

    @Test
    public void removeFromCartError() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(WRONG_USERNAME);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(WRONG_USERNAME);
        response = cartController.removeFromcart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
