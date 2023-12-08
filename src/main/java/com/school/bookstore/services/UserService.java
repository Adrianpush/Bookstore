package com.school.bookstore.services;

import com.school.bookstore.models.dtos.JwtAuthenticationResponseDTO;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    void deleteUser(Long id);

    UserDetailsService userDetailsService();

    User save(User newUser);

    void checkForDuplicate(String email);
}