package com.school.bookstore.services;


import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.exceptions.UserNotFoundException;
import com.school.bookstore.models.dtos.UserDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BookRepository bookRepository;

    public UserServiceImpl(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(convertToUserEntity(userDTO));
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserById(Long userID) {
        return convertToUserDTO(userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getId())) {
            User updatedUserEntity = userRepository.save(convertToUserEntity(userDTO));
            return convertToUserDTO(updatedUserEntity);
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public Long deleteUser(Long userID) {
        if (userRepository.existsById(userID)) {
            userRepository.deleteById(userID);
            return userID;
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public void addToFavorites(Long userId, Long bookId) throws UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("User not found"));
        user.getFavoriteBooks().add(book);
        book.getFavorites().add(user);
        userRepository.save(user);
        bookRepository.save(book);
    }

    private User convertToUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEMail(userDTO.getEMail());
        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEMail(user.getEMail());
        return userDTO;
    }
}
