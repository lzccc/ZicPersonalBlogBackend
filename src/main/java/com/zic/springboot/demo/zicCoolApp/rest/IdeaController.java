package com.zic.springboot.demo.zicCoolApp.rest;

import com.zic.springboot.demo.zicCoolApp.exceptions.UserNotFoundExeception;
import com.zic.springboot.demo.zicCoolApp.services.ExternalAPIService;
import com.zic.springboot.demo.zicCoolApp.services.response.UserPojo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@CrossOrigin("*")
@RestController
public class IdeaController {
    @Autowired
    private ExternalAPIService externalAPIService;
    @GetMapping("/api/idea")
    public String getIdea() {
        return externalAPIService.fetchQuote();
    }



    Set<UserPojo> result;
    //Load the data once
    @PostConstruct
    public void loadData(){
        result = new HashSet<>();
        UserPojo userPojo1 = new UserPojo("Zichong", "Li", "test@email");
        UserPojo userPojo2 = new UserPojo("Zichong2", "Li2", "test2@email");
        UserPojo userPojo3 = new UserPojo("Zichong3", "Li3", "test3@email");
        result.add(userPojo1);
        result.add(userPojo2);
        result.add(userPojo3);
    }

    //From the client set, returning a Set or list will both result in a list of User json.
    @GetMapping("/user")
    public Set<UserPojo> getUsers() {
        result.clear();
        if (result.isEmpty()) {
            throw new UserNotFoundExeception("The number of user is 0");
        }
        return result;
    }

    //Add an Exception handler using @ExceptionHandler
    //This method can catch and response to UserNotFoundException
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundExeception ex) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        userErrorResponse.setMessage(ex.getMessage());
        userErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    //Add another Exception handler to catch all exceptions
    //This handler is only by this particular rest controller.
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(Exception ex) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        userErrorResponse.setMessage(ex.getMessage());
        userErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
}
