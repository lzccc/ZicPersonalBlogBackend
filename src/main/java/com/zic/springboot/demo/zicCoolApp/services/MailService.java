package com.zic.springboot.demo.zicCoolApp.services;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zic.springboot.demo.zicCoolApp.services.response.MailData;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MailService {
    public Response sentWelcomeEmail(MailData mailData) {
        String name = mailData.getName();
        String email = mailData.getEmail();
        String thankYouLetter = "Dear " + name + ",\\n\\n" +
                "I hope this message finds you well. I wanted to take a moment to express my sincere gratitude for your interest in me. It truly means a lot to me.\\n" +
                "Your support and consideration have not gone unnoticed, and I deeply appreciate the time and effort you have taken to show your interest.\\n" +
                "I would like to assure you that I will make it a priority to get in touch with you as soon as possible to further discuss our next steps.\\n" +
                "Please feel free to reach out to me if you have any further questions or if there is anything else you would like to discuss. I look forward to speaking with you soon.\\n\\n" +
                "Best regards,\\nZichong";
        OkHttpClient client = new OkHttpClient();
        com.squareup.okhttp.MediaType mediaType = com.squareup.okhttp.MediaType.parse("application/json");
        com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(mediaType, "{\"from\":{\"email\":\"mailtrap@lizichong.space\",\"name\":\"From Lizichong.Space\"},\"to\":[{\"email\":\"" + email + "\"}],\"subject\":\"You are awesome!\",\"text\":\"" + thankYouLetter + "\",\"category\":\"Welcome letter\"}");
        Request mailRequest = new Request.Builder()
                .url("https://send.api.mailtrap.io/api/send")
                .method("POST", body)
                .addHeader("Authorization", "Bearer 74bd86aa7a54bcd0fc9a4307ac6415b1")
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(mailRequest).execute();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
