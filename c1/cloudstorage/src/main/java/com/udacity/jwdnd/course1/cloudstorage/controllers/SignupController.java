package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    Logger logger = LoggerFactory.getLogger(SignupController.class);
    @Autowired
    UserService userService;

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String signUp(@ModelAttribute User user, Model model) {
        if(!userService.isUserNameAvailable(user.getUsername())){
            logger.info("Username already present");
            model.addAttribute("signupError", "Username is already taken.");
            return "signup";
        } else {
            userService.createNewUser(user);
            model.addAttribute("signupSuccess", "You successfully signed up!");
        }
        return "redirect:/login";
    }
}
