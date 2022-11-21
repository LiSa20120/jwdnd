package com.example.demo.controllers;

import com.example.demo.EcommerceTestUtil;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    String USER_NAME = "testUsername";
    String PASSWORD = "testPassword";
    String HASHED_PASSWORD = "hashedPassword";
    String WRONG_USERNAME = "wrongUsername";
    String WRONG_PASSWORD = "wrongPassword";

    @Before
    public void setup() {
        userController = new UserController();
        EcommerceTestUtil.injectObjects(userController, "userRepository", userRepository);
        EcommerceTestUtil.injectObjects(userController, "cartRepository", cartRepository);
        EcommerceTestUtil.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUserSuccess() {
        when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD);
        CreateUserRequest userRequest = createUserRequest();
        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(USER_NAME, user.getUsername());
        assertEquals(HASHED_PASSWORD, user.getPassword());
    }

    @Test
    public void createUserError() {
        CreateUserRequest userRequest = createUserRequest();
        userRequest.setPassword(WRONG_PASSWORD);
        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findUserSuccess() {
        User user = createUser();
        when(userRepository.findByUsername(USER_NAME)).thenReturn(user);
        CreateUserRequest userRequest = createUserRequest();
        userController.createUser(userRequest);
        ResponseEntity<User> response = userController.findByUserName(USER_NAME);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_NAME, user.getUsername());
    }

    @Test
    public void findUserError() {
        ResponseEntity<User> response = userController.findByUserName(WRONG_USERNAME);
        assertNotNull(userController.findByUserName(WRONG_USERNAME));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdSuccess() {
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(USER_NAME, Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void findByIdError() {
        User user = createUser();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> response = userController.findById(5L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private CreateUserRequest createUserRequest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(USER_NAME);
        userRequest.setPassword(PASSWORD);
        userRequest.setConfirmPassword(PASSWORD);
        return userRequest;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername(USER_NAME);
        user.setPassword(PASSWORD);
        return user;
    }
}
