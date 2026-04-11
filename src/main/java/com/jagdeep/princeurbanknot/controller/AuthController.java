package com.jagdeep.princeurbanknot.controller;

import com.jagdeep.princeurbanknot.config.JwtUtil;
import com.jagdeep.princeurbanknot.model.User;
import com.jagdeep.princeurbanknot.model.Role;
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        // Default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        User saved = userService.registerUser(user);
        // Return safe response (no password)
        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "email", saved.getEmail(),
                "role", saved.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String token = userService.loginUser(email, request.get("password"));
        User user = userService.getUserByEmail(email); // ← add this
        return ResponseEntity.ok(Map.of(
                "token", token,
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }
}