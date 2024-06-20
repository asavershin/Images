package com.github.asavershin.worker.out;

import java.io.InputStream;

public interface MinioService {
    void saveFile(byte[] image, String filename);

    InputStream getFile(String link);
}
