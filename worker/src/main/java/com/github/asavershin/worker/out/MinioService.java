package com.github.asavershin.images.out;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {
    void saveFile(byte[] image, String filename);

    InputStream getFile(String link);
}
