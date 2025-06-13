package com.example.ProjetoJava.controller;

import com.example.ProjetoJava.dto.RegisterRequest;
import com.example.ProjetoJava.dto.UserResponse;
import com.example.ProjetoJava.entity.User;
import com.example.ProjetoJava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<UserResponse> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userService.loadUserByUsername(email);
        return ResponseEntity.ok(mapToUserResponse(user));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody RegisterRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userService.loadUserByUsername(email);
        if (!user.getEmail().equals(request.getEmail())) {
            throw new RuntimeException("Usuário só pode editar seu próprio perfil");
        }
        User updatedUser = userService.updateUser(user.getId(), request);
        return ResponseEntity.ok(mapToUserResponse(updatedUser));
    }

    @GetMapping("/admin/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/admin/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody RegisterRequest request) {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(mapToUserResponse(updatedUser));
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserById(id);
        if (user.getEmail().equals(email)) {
            throw new RuntimeException("Admin não pode deletar a si mesmo");
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}