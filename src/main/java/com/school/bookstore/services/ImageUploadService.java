package com.school.bookstore.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    String uploadImage(MultipartFile multipartFile, String fileName);

    String updateImage(MultipartFile multipartFile, String fileName);
}
