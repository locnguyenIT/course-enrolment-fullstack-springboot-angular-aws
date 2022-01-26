package com.example.demo.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Service
public class AWSMailService {

    private final AmazonSimpleEmailService emailService;

    public void send(String to, String content) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(content)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData("Reset Password")))
                    .withSource("your-email");
            emailService.sendEmail(request);
        } catch (AmazonSimpleEmailServiceException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }

}

