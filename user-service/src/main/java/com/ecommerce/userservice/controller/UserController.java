package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest newUser) {
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    }

    @PutMapping( "/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id, @RequestBody UserRequest user) {
         boolean updated = userService.updateUser(id, user);
         if(updated) {
             return ResponseEntity.ok("User updated successfully");
         }
         return ResponseEntity.notFound().build();
    }


}
