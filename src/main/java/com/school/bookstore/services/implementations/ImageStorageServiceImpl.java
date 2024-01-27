package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.book.ImageStorageException;
import com.school.bookstore.exceptions.book.InvalidImageException;
import com.school.bookstore.services.interfaces.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final String FAILED_UPLOAD = "Unable to upload file";
    private final String bucketName;
    private final String projectId;
    private final String apiKey;
    private final OkHttpClient client;
    private final String imageBaseUrl;

    public ImageStorageServiceImpl(@Value("${supabase.apikey}") String apiKey, @Value("${image.urlBase}") String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
        this.client = new OkHttpClient();
        this.projectId = "dkckcusqogzbwetnizwe";
        this.bucketName = "books";
        this.apiKey = apiKey;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile, String fileName) {
        verifyFile(multipartFile);
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(
                                    multipartFile.getBytes(),
                                    MediaType.parse(Objects.requireNonNull(multipartFile.getContentType()))))
                    .build();
        } catch (IOException e) {
            throw new ImageStorageException(FAILED_UPLOAD);
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageStorageException(FAILED_UPLOAD);
        }
    }

    @Override
    public String updateImage(MultipartFile multipartFile, String fileName) {
        verifyFile(multipartFile);
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(multipartFile.getBytes(),
                                    MediaType.parse(Objects.requireNonNull(multipartFile.getContentType()))))
                    .build();
        } catch (IOException e) {
            throw new ImageStorageException(FAILED_UPLOAD);
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .put(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageStorageException(FAILED_UPLOAD);
        }
    }

    @Override
    public void deleteImageByBookId(Long bookId) {
        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, bookId.toString()))
                .addHeader("Authorization", "Bearer " + apiKey)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            log.info(response.toString());
        } catch (IOException exception) {
            throw new ImageStorageException("Unable to delete image");
        }
    }

    private void verifyFile(MultipartFile file) {
        if (!file.isEmpty()) {
            verifySizeLimit(file, 1000);
            verifyJPG(file);
        } else {
            throw new InvalidImageException("File is null");
        }
    }

    private void verifyJPG(MultipartFile file) {
        if (!"jpeg".equalsIgnoreCase(Objects.requireNonNull(file.getContentType()).split("/")[1])) {
            throw new InvalidImageException("Invalid image format");
        }
    }

    private void verifySizeLimit(MultipartFile file, long sizeLimitKB) {
        if (file.getSize() > sizeLimitKB * 1024) {
            throw new InvalidImageException("File too large. Limit is " + sizeLimitKB + "kilobytes");
        }
    }
}