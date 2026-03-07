package com.jagdeep.princeurbanknot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.jagdeep.princeurbanknot.model.User;
import com.jagdeep.princeurbanknot.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.saveUser(user);
    }
}