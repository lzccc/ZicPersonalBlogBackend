package com.zic.springboot.demo.zicCoolApp.rest;

import com.squareup.okhttp.Response;
import com.zic.springboot.demo.zicCoolApp.aspect.RateLimiter;
import com.zic.springboot.demo.zicCoolApp.services.LocalFileService;
import com.zic.springboot.demo.zicCoolApp.services.MailService;
import com.zic.springboot.demo.zicCoolApp.services.RedisService;
import com.zic.springboot.demo.zicCoolApp.services.response.MailData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MyRestController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LocalFileService localFileService;

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public MyRestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }

    @GetMapping("/test")
    public String sayTest() {
        return "Test!";
    }

    @GetMapping("/api/cv")
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

    @GetMapping("/api/blog")
    public Map<String, Object> getBlog(@RequestParam("blogid") String blogId) {
        Map<String, Object> value = redisService.getHash(blogId);
        return value;
    }

    //http://localhost:8080/bloglist?listid=listTest
    //ToDO:Add another parameter that control how many blog we want to get.
    @GetMapping("/api/bloglist")
    public List<Map<String, Object>> getBlogList(@RequestParam("listid") String listId) {
        List<Object> value = redisService.getAllFromZSet(listId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object key : value) {
            Map<String, Object> hash = redisService.getHash((String)key);
            result.add(hash);
        }
        return result;
    }

    @GetMapping("/api/comments")
    public List<Map<String, Object>> getCommentList(@RequestParam("listid") String listId) {
        List<Object> value = redisService.getAllFromZSet(listId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object key : value) {
            Map<String, Object> hash = redisService.getHash((String)key);
            result.add(hash);
        }
        return result;
    }

    @PostMapping("/api/comment/{userid}")
    public ResponseEntity<Map<String, Object>> saveComment(@PathVariable String userid, @RequestBody Map<String, Object> request) {
        try {
            //First create the blogID
            String newkey = redisService.createComment(userid, request);
            Map<String, Object> result = redisService.getHash(newkey);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        try {
            //First create the blogID
            redisService.deleteComment(commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/mdfile")
    public ResponseEntity<String> getMDFileContent(@RequestParam("mdfileid") String mdfileid) {
        try {
            // Due to the memory limit, comment this out to get the content from disk
            // String mdContent = (String) redisService.getValue("blogContent:" +  mdfileid);
            String mdContent = localFileService.getContent("blogContent:" +  mdfileid);
            return ResponseEntity.ok().body(mdContent);
        } catch (Exception exc) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error!");
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error!");
        }
    }

    @PostMapping("/api/mdfile/{mdfileid}")
    public ResponseEntity<Void> saveContent(@PathVariable String mdfileid, @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            // Due to the memory limit, comment this out to use the disk space for all blog contents
            // redisService.setValue("blogContent:" + mdfileid, content);

            //Using disk space
            localFileService.saveContent("blogContent:" + mdfileid, content);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/api/mdfile/{mdfileid}")
    public ResponseEntity<Void> deletePost(@PathVariable String mdfileid) {
        System.out.println(mdfileid);
        try {
            //First create the blogID
            redisService.deleteBlog(mdfileid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/mdfile/createblog/{userid}")
    public ResponseEntity<String> createBlog(@PathVariable String userid, @RequestBody Map<String, Object> request) {
        try {
            //First create the blogID
            String blogID = redisService.createUserKey(userid, request);
            return ResponseEntity.ok().body(blogID);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RateLimiter(value = 5, timeWindow = 10000)
    @GetMapping("/api/test")
    public void test1(HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getRemoteAddr());
        System.out.println(httpServletRequest.getHeader("X-Forwarded-For"));
        System.out.println(httpServletRequest.getHeader("X-Real-IP"));
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
    }

    @PostMapping("/api/email")
    public ResponseEntity<String> sendEmails(@RequestBody MailData mailData) {
        //send welcome letter first
        Response response = mailService.sentWelcomeEmail(mailData);
        return String.valueOf(response.code()).startsWith("2")? ResponseEntity.ok().build() : ResponseEntity.status(500).build();
    }

    // This method handles incoming messages from the frontend
    // Demo for websocket
    @PostMapping("/api/send-message")
    public String handleMessage(String message) {
        // Process the received message here (you can access the message content using message.getContent())
        // You can also send a response back to the frontend if needed
        System.out.println("zicreceived1!");
        messagingTemplate.convertAndSend("/api/topic/messages", "test1");
        messagingTemplate.convertAndSend("/api/topic/messages", "test2");
        return "Response from server: " + "test";
    }

//    @SendTo("/api/topic/messages") // Send the response to the specified destination
//    public String broadcastMessage(@Payload String textMessageDTO) {
//        System.out.println("zicreceived2!");
//        return textMessageDTO;
//    }
}
