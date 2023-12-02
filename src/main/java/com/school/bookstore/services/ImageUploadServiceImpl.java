package com.school.bookstore.services;

import com.school.bookstore.exceptions.ImageUploadException;
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

    @Override
    public String uploadImage(MultipartFile multipartFile, String fileName) {
        verifyFile(multipartFile);
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse(Objects.requireNonNull(multipartFile.getContentType())), multipartFile.getBytes()))
                    .build();
        } catch (IOException e) {
            throw new ImageUploadException("Unable to upload file");
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("Unable to upload file");
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
                            RequestBody.create(MediaType.parse(Objects.requireNonNull(multipartFile.getContentType())), multipartFile.getBytes()))
                    .build();
        } catch (IOException e) {
            throw new ImageUploadException("Unable to upload file");
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .put(requestBody)
                .build();
        log.info("sending request");
        try (Response response = client.newCall(request).execute()) {
            log.info("request successful");
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("Unable to upload file");
        }
    }

    private void verifyFile(MultipartFile file) {
        if (!isJPEG(file) || !isUnderSizeLimit(file, 1000)) {
            throw new ImageUploadException("Unable to upload file");
        }
    }

    private static boolean isJPEG(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null && "jpeg".equalsIgnoreCase(Objects.requireNonNull(file.getContentType()).split("/")[1]);
        } catch (IOException e) {
            log.info("File upload failed");
        }
        return false;
    }

    private static boolean isUnderSizeLimit(MultipartFile file, long sizeLimitKB) {
        return file.getSize() <= sizeLimitKB * 1024;
    }
}
