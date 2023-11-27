package com.school.bookstore.services;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        log.info("uploading image");
        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse(multipartFile.getContentType()), multipartFile.getBytes()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Request request = new Request.Builder()
                .url(String.format("https://%s.supabase.co/storage/v1/object/%s/%s", projectId, bucketName, fileName))
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();
        log.info("sending request");
        try (Response response = client.newCall(request).execute()) {
            log.info("request successful");
            return imageBaseUrl.concat(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
