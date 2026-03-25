package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.config.JwtUtil;
import com.jagdeep.princeurbanknot.model.User;
import com.jagdeep.princeurbanknot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String token = userService.loginUser(request.get("email"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}