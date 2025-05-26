package com.example.demo.Controller.User;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    private String UPLOAD_DIR = "uploads/";

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            // Lấy đường dẫn tương đối của file

            // Đọc nội dung của file
            byte[] imageBytes = Files.readAllBytes(Paths.get(UPLOAD_DIR,filename));

            // Trả về file ảnh
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // Thay đổi nếu định dạng ảnh khác
                    .body(imageBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
