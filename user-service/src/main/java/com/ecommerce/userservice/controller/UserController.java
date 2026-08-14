package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Fetching all users");

        List<UserResponse> users = userService.fetchAllUsers();

        log.info("Users successfully fetched - count: {}", users.size());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String id
    ) {
        log.info("Fetching user - userId: {}", id);

        UserResponse user = userService.getUser(id);

        log.info("User successfully fetched - userId: {}", id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody UserRequest newUser
    ) {
        log.info("Creating new user");

        userService.createUser(newUser);

        log.info("User successfully created");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest user
    ) {
        log.info("Updating user - userId: {}", id);

        boolean updated = userService.updateUser(id, user);

        if (updated) {
            log.info("User successfully updated - userId: {}", id);

            return ResponseEntity.ok("User updated successfully");
        }

        log.warn("User not found - userId: {}", id);

        return ResponseEntity.notFound().build();
    }
}