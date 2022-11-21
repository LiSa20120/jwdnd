package com.example.demo.controllers;

import com.example.demo.EcommerceTestUtil;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        EcommerceTestUtil.injectObjects(itemController, "itemRepository", itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal("23.34"));
        item.setDescription("itemDescription");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName("itemName")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void getItemByIdSuccess() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemByIdError() {
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getItemsByNameSuccess() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemsByNameError() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemNewName");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
