package com.zic.springboot.demo.zicCoolApp.services;

import com.zic.springboot.demo.zicCoolApp.services.response.DailyQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class ExternalAPIService {
    private RestTemplate restTemplate;
    private RedisService redisService;

    //Recommend to use constructor injection, because field injection is hard for unit test.
    //Qualifier has the same name as the class we want to inject, except the first letter is
    //lower case.
    @Autowired
    public ExternalAPIService(@Qualifier("restTemplate") RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    public String fetchQuote() {
        String dailyQuote = "daily:quote:";
        String externalAPI = "https://zenquotes.io/api/random?";
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM:dd:yyyy");
        String formattedDate = currentDate.format(formatter);
        dailyQuote += formattedDate;
        //ToDo: Surround it with try/catch to prevent the timeout of Redis socket
        String quoteString = (String)redisService.getValue(dailyQuote);
        if (quoteString != null) {
            return quoteString;
        }
        String newQuote = fetchFromExternalAPI(externalAPI);
        if (newQuote != null) {
            redisService.setValueWithTTL(dailyQuote, newQuote, 1, TimeUnit.DAYS);
            return newQuote;
        }
        return "Something went wrong...";
    }

    private String  fetchFromExternalAPI(String url) {
        ResponseEntity<DailyQuote[]> response = restTemplate.getForEntity(url, DailyQuote[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            DailyQuote[] dailyQuotes = response.getBody();
            return dailyQuotes[0].getQ() + "\n---" + dailyQuotes[0].getA();
        } else {
            // Handle error response
            return null;
        }
    }
}
