package com.github.asavershin.api.application.out;

import java.util.List;

public interface FileService<T, K> {
    /**
     * Saves a file to the storage.
     *
     * @param file The file object to be saved.
     * @return The saved file object.
     */
    T saveFile(K file);
    /**
     * Retrieves the content of a file from the storage.
     *
     * @param link The unique identifier or link of the file.
     * @return The content of the file as a byte array.
     */
    byte[] getFile(String link);
    /**
     * Deletes multiple files from the storage.
     *
     * @param files A list of file links to be deleted.
     */
    void deleteFiles(List<String> files);
}
