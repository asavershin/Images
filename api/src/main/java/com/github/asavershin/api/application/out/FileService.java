package com.github.asavershin.api.application.out;

import java.util.List;

public interface FileService<T, K> {
    T saveFile(K file);
    byte[] getFile(String link);
    void deleteFiles(List<String> files);
}
