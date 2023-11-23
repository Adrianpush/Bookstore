package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.exceptions.UserNotFoundException;
import com.school.bookstore.models.dtos.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userID);

    UserDTO updateUser(UserDTO userDTO);

    Long deleteUser(Long userID);

    void addToFavorites(Long userId, Long bookId) throws UserNotFoundException, BookNotFoundException;
}
