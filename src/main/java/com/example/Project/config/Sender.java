package com.example.Project.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
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

}