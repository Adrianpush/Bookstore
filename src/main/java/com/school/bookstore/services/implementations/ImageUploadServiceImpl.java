package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.book.ImageUploadException;
import com.school.bookstore.services.interfaces.ImageUploadService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private static final String FAILED_UPLOAD = "Unable to upload file";
    private final String bucketName;
    private final String projectId;
    private final String apiKey;
    private final OkHttpClient client;
    private final String imageBaseUrl;

    public ImageUploadServiceImpl(@Value("${supabase.apikey}") String apiKey, @Value("${image.urlBase}") String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
        this.client = new OkHttpClient();
        this.projectId = "dkckcusqogzbwetnizwe";
        this.bucketName = "books";
        this.apiKey = apiKey;
    }

    private static boolean isJPEG(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null && "jpeg".equalsIgnoreCase(Objects.requireNonNull(file.getContentType()).split("/")[1]);
        } catch (IOException e) {
            log.info(FAILED_UPLOAD);
        }
        return false;
    }

    private static boolean isUnderSizeLimit(MultipartFile file, long sizeLimitKB) {
        return file.getSize() <= sizeLimitKB * 1024;
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
            throw new ImageUploadException(FAILED_UPLOAD);
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageUploadException(FAILED_UPLOAD);
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
            throw new ImageUploadException(FAILED_UPLOAD);
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .put(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageUploadException(FAILED_UPLOAD);
        }
    }

    private void verifyFile(MultipartFile file) {
        if (!isJPEG(file) || !isUnderSizeLimit(file, 1000)) {
            throw new ImageUploadException("Wrong format or size to large.");
        }
    }
}