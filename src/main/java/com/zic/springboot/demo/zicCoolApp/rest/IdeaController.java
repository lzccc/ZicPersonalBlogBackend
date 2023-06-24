package com.zic.springboot.demo.zicCoolApp.rest;

import com.zic.springboot.demo.zicCoolApp.services.ExternalAPIService;
import com.zic.springboot.demo.zicCoolApp.services.response.DailyQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin("*")
@RestController
public class IdeaController {
    @Autowired
    private ExternalAPIService externalAPIService;
    @GetMapping("/idea")
    public String getIdea() {
        return externalAPIService.fetchQuote();
    }
}
