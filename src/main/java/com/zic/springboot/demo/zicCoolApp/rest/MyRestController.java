package com.zic.springboot.demo.zicCoolApp.rest;

import com.zic.springboot.demo.zicCoolApp.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
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

    @GetMapping("/blog")
    public Map<String, Object> getBlog(@RequestParam("blogid") String blogId) {
        Map<String, Object> value = redisService.getHash(blogId);
        return value;
    }

    //http://localhost:8080/bloglist?listid=listTest
    //ToDO:Add another parameter that control how many blog we want to get.
    @GetMapping("/bloglist")
    public List<Map<String, Object>> getBlogList(@RequestParam("listid") String listId) {
        List<Object> value = redisService.getAllFromZSet(listId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object key : value) {
            System.out.println((String)key);
            Map<String, Object> hash = redisService.getHash((String)key);
            result.add(hash);
        }
        return result;
    }

    @GetMapping("/mdfile")
    public ResponseEntity<String> getMDFileContent(@RequestParam("mdfileid") String mdfileid) {
        try {
            String mdContent = (String) redisService.getValue("blogContent:" +  mdfileid);
            return ResponseEntity.ok().body(mdContent);
        } catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error!");
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error!");
        }
    }

    @PostMapping("/mdfile/save/{mdfileid}")
    public ResponseEntity<Void> saveContent(@PathVariable String mdfileid, @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            redisService.setValue("blogContent:" + mdfileid, content);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mdfile/createblog/{userid}")
    public ResponseEntity<String> createBlog(@PathVariable String userid, @RequestBody Map<String, Object> request) {
        try {
            //First create the blogID
            String blogID = redisService.createUserKey( userid, request);

            return ResponseEntity.ok().body(blogID);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
