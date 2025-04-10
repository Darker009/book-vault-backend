package org.tech.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tech.dto.LoginRequest;
import org.tech.dto.RegisterRequest;
import org.tech.dto.UpdateProfileRequest;
import org.tech.dto.UserResponse;
import org.tech.entity.User;
import org.tech.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = userService.registerUser(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = userService.loginUser(request);
            User user = (User) result.get("user");
            String token = (String) result.get("token");

            UserResponse response = new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.isProfileUpdated(),
                    user.getUserProfile()
            );

            Map<String, Object> finalResponse = new HashMap<>();
            finalResponse.put("token", token);
            finalResponse.put("user", response);

            return ResponseEntity.ok(finalResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserResponse response = new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.isProfileUpdated(),
                    user.getUserProfile()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            String message = userService.updateProfile(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/students")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllStudents() {
        List<UserResponse> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

}
