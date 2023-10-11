package com.example.zahnbonusproject.Controller;

import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmailAndPassword(email, password);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

        return ResponseEntity.ok(user);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User savedUser = userService.registerUser(user);
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }


}

