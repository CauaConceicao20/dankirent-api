package com.dankirent.api.service.interfaces;

import com.dankirent.api.infrastructure.storage.FileMetaData;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

    void uploadFile(MultipartFile file, String fileName);
    FileMetaData getMetaData(String fileName);
    void deleteFile(String fileName);

}
