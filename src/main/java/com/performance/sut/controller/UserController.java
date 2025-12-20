package com.performance.sut.controller;

import com.performance.sut.entity.User;
import com.performance.sut.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request.getName(), request.getEmail()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable @NonNull Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request.getName(), request.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CreateUserRequest {
        private String name;
        private String email;
    }

    @Data
    public static class UpdateUserRequest {
        private String name;
        private String email;
    }
}
