package com.example.Project.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AwsSesService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
 
    public AwsSesService(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }
 
    public void send(String subject, Map<String, Object> variables, String... to) {
        String content = "hello";
        
        SendEmailRequest sendEmailRequest = createSendEmailRequest(subject, content, to);
        
        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }
 
    private SendEmailRequest createSendEmailRequest(String subject, String content, String... to) {
        return new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withSource("AAA")
                .withMessage(new Message()
                    .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(subject))
                    .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
                );
    }

}