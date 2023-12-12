package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.users.AuthentificationException;
import com.school.bookstore.exceptions.users.UserCreateException;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.models.enums.Role;
import com.school.bookstore.repositories.UserRepository;
import com.school.bookstore.services.interfaces.EmailService;
import com.school.bookstore.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User %s not found.";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        checkForDuplicate(userDTO.getEmail());

        User user = User
                .builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .address(userDTO.getAddress())
                .role(Role.ROLE_USER)
                .build();

        user = save(user);
        emailService.sendWelcomeEmail(user.getFullName(), user.getEmail());

        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserById(String requesterEmail, Long requestedId) {
        User user;
        if (requestedId == null) {
            user = userRepository.findByEmail(requesterEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted(requesterEmail)));
        } else {
            user = userRepository.findById(requestedId)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted(requestedId)));
            validateRequest(requesterEmail, user);
        }

        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .toList();
    }

    @Override
    public void deleteUser(String requesterEmail, Long requestedId) {
        User user;
        if (requestedId == null) {
            user = userRepository.findByEmail(requesterEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted(requesterEmail)));
        } else {
            user = userRepository.findById(requestedId)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted(requestedId)));
            validateRequest(requesterEmail, user);
        }

        User anon = User.builder()
                .id(user.getId())
                .orderHistory(user.getOrderHistory())
                .email("anon" + user.getId() + "@anon.anon")
                .fullName("anon")
                .address("anon")
                .password("anon")
                .role(Role.ROLE_EXPIRED)
                .build();

        userRepository.save(anon);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.formatted("")));
    }

    @Override
    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());

        return userDTO;
    }

    @Override
    public void checkForDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserCreateException("Email already in use");
        }
    }

    private void validateRequest(String email, User user) {
        if (user.getRole() == Role.ROLE_USER && !user.getEmail().equals(email)) {
            throw new AuthentificationException("Not allowed to access this resource.");
        }
    }
}

