package com.zic.springboot.demo.zicCoolApp.services;

import com.zic.springboot.demo.zicCoolApp.services.response.DailyQuote;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public ExternalAPIService(RestTemplate restTemplate, RedisService redisService) {
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
            return dailyQuotes[0].getQ() + "  ---" + dailyQuotes[0].getA();
        } else {
            // Handle error response
            return null;
        }
    }
}
