package com.namira.myhealth.controller;

import com.namira.myhealth.model.User;
import com.namira.myhealth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user) {
        // Check username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username already taken. Please choose another.");
            return ResponseEntity.badRequest().body(error);
        }

        // Validate password strength
        String passwordError = validatePassword(user.getPassword());
        if (passwordError != null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", passwordError);
            return ResponseEntity.badRequest().body(error);
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        // Never return the password, even hashed
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number.";
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return "Password must contain at least one special character.";
        }
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Find the user by username
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password.");
            return ResponseEntity.status(401).body(error);
        }

        User user = userOptional.get();

        // Check the password against the stored hash
        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password.");
            return ResponseEntity.status(401).body(error);
        }

        // Login successful - return user info (without password)
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}