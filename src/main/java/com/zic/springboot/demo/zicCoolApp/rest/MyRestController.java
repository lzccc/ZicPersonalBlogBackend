package com.zic.springboot.demo.zicCoolApp.rest;

import com.zic.springboot.demo.zicCoolApp.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class MyRestController {
    @Autowired
    private RedisService redisService;
    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }

    @GetMapping("/test")
    public String sayTest() {
        return "Test!";
    }

    @GetMapping("/download/cv")
    public ResponseEntity<Resource> downloadFile() {
        // Load the file as a Resource (e.g., from the file system or database)
        String filePath = "/Users/zichongli/Downloads/ZichongLi_Resume.pdf";
        Resource fileResource = new FileSystemResource(filePath);

        // Set the desired file name
        String fileName = "cv2.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileResource);
    }

    @GetMapping("/redis")
    public String getRedis() {
        String value = (String) redisService.getValue("k");
        return value;
    }
}
