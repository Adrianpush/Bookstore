package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Validated @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok("User with id ".concat(userService.deleteUser(id).toString().concat(" was deleted.")));
    }

    @PostMapping("/filter")
    public ResponseEntity<String> addBookToFavorites(@RequestParam Long userId, @RequestParam Long bookId) {
        userService.addToFavorites(userId, bookId);
        return ResponseEntity.ok("Book with id " + userId + " added to favorite list of user with id " + userId);
    }
}
