package com.school.bookstore.services;

import com.school.bookstore.exceptions.CustomerCreateException;
import com.school.bookstore.exceptions.CustomerNotFoundException;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createCustomer(UserDTO userDTO) {
        checkForDuplicate(userDTO.getEmail());
        User user = userRepository.save(convertToCustomerEntity(userDTO));

        return convertToCustomerDTO(user);
    }

    @Override
    public UserDTO getCustomerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        return convertToCustomerDTO(user);
    }

    @Override
    public List<UserDTO> getAllCustomers() {
        return userRepository.findAll().stream()
                .map(this::convertToCustomerDTO)
                .toList();
    }

    @Override
    public void deleteCustomer(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    private User convertToCustomerEntity(UserDTO userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        List<OrderItem> shoppingCart = new ArrayList<>();
        user.setShoppingCart(shoppingCart);
        List<Order> orderHistory = new ArrayList<>();
        user.setOrderHistory(orderHistory);

        return user;
    }

    private UserDTO convertToCustomerDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setAddress(user.getAddress());

        return userDTO;
    }

    private void checkForDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomerCreateException("E-mail already in use");
        }
    }
}

