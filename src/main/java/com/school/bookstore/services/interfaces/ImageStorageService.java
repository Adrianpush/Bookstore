package com.school.bookstore.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String uploadImage(MultipartFile multipartFile, String fileName);

    String updateImage(MultipartFile multipartFile, String fileName);

    void deleteImageByBookId(Long bookId);
}