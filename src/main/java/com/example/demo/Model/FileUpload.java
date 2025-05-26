package com.example.demo.Model;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
    private MultipartFile file;

    public FileUpload() {
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
