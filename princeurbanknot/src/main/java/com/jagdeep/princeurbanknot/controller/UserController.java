package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.model.User;
import com.jagdeep.princeurbanknot.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
}