package com.example.Project.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mail {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    public static SendRawEmailRequest getSendRawEmailRequest(String title, String content, String receiver, String html, String fileRoot) throws MessagingException, IOException {

    // title : 메일 제목
    // content : 안에 내용
    // receiver : 받는 사람
    // html : 이메일 템플릿
    // fileRoot : 파일 경로

    Session session = Session.getDefaultInstance(new Properties());
    MimeMessage message = new MimeMessage(session);

    // Define mail title
    message.setSubject(title);

    // Define mail Sender
    message.setFrom("Hello");

    // Define mail Receiver
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

    // Create a multipart/alternative child container.
    MimeMultipart msg_body = new MimeMultipart("alternative");

    // Create a wrapper for the HTML and text parts.
    MimeBodyPart wrap = new MimeBodyPart();

    // Define the text part.
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setContent(content, "text/plain; charset=UTF-8");

    // Define the HTML part.
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(html, "text/html; charset=UTF-8");

    // Add the text and HTML parts to the child container.
    msg_body.addBodyPart(textPart);
    msg_body.addBodyPart(htmlPart);

    // Add the child container to the wrapper object.
    wrap.setContent(msg_body);

    // Create a multipart/mixed parent container.
    MimeMultipart msg = new MimeMultipart("mixed");

    // Add the parent container to the message.
    message.setContent(msg);

    // Add the multipart/alternative part to the message.
    msg.addBodyPart(wrap);

    // Define the attachment
    MimeBodyPart att = new MimeBodyPart();

    // Add the attachment to the message.
    msg.addBodyPart(att);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    message.writeTo(outputStream);
    RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
    return new SendRawEmailRequest(rawMessage);

    }

}