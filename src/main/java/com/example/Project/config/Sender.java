package com.example.Project.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.example.Project.dto.SenderDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Sender {
	
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private String from;

    public Sender (AmazonSimpleEmailService amazonSimpleEmailService) {
    	this.amazonSimpleEmailService = amazonSimpleEmailService;
    	this.from = "alphathx13@gmail.com";
    }
    
    public void send(String subject, String content, List<String> receivers) {
        if(receivers.size() == 0) {
            return;
        }
        
        SenderDto senderDto = SenderDto.builder()
                .from(from)
                .to(receivers)
                .subject(subject)
                .content(content)
                .build();

        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(senderDto.toSendRequestDto());

        if(sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            log.info("[AWS SES] 메일전송완료 => " + senderDto.getTo());
        } else {
            log.error("[AWS SES] 메일전송 중 에러가 발생했습니다: {}", sendEmailResult.getSdkResponseMetadata().toString());
            log.error("발송실패 대상자: " + senderDto.getTo() + " / subject: " + senderDto.getSubject());
        }
    }
    
    public void templateSend(String subject, String templatePath, Map<String, String> variables, List<String> receivers) {
        if (receivers.isEmpty()) {
            return;
        }
        
        // Read HTML content from classpath resource
        String htmlContent;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                log.error("Template file not found: " + templatePath);
                return;
            }
            htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read HTML template file", e);
            return;
        }

        // Replace placeholders in the HTML content
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            htmlContent = htmlContent.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        SendEmailRequest sendEmailRequest = new SendEmailRequest()
            .withSource(from)
            .withDestination(new Destination().withToAddresses(receivers))
            .withMessage(new Message()
                .withSubject(new Content().withCharset("UTF-8").withData(subject))
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlContent))));

        SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(sendEmailRequest);

        if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {
            log.info("[AWS SES] 메일전송완료 => " + receivers);
        } else {
            log.error("[AWS SES] 메일전송 중 에러가 발생했습니다: {}", sendEmailResult.getSdkResponseMetadata().toString());
            log.error("발송실패 대상자: " + receivers + " / subject: " + subject);
        }
    }

}