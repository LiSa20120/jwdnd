package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserMapper userMapper;
    @Autowired
    HashService hashService;

    public boolean isUserNameAvailable(String username) {
        logger.info("Checking if username={} is available", username);
        return userMapper.findUserByName(username) == null;
    }

    public int createNewUser(User user) {
        var random = new SecureRandom();
        var salt = new byte[16];
        random.nextBytes(salt);
        var encodedSalt = Base64.getEncoder().encodeToString(salt);
        user.setSalt(encodedSalt);
        var hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        user.setPassword(hashedPassword);
        var userId = userMapper.insertNewUser(user);
        logger.info("New user created with userName={}", user.getUsername());
        return userId;
    }

    public User getUserByUserName(String userName) {
        return userMapper.findUserByName(userName);
    }
}
