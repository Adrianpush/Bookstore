package com.school.bookstore.services;

import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDTO createCustomer(UserDTO userDTO);

    UserDTO getCustomerById(Long id);

    List<UserDTO> getAllCustomers();

    void deleteCustomer(Long id);

    UserDetailsService userDetailsService();

    User save(User newUser);
}