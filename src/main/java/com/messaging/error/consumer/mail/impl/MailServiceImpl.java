package com.messaging.error.consumer.mail.impl;

import com.messaging.error.consumer.data.ErrorMessage;
import com.messaging.error.consumer.mail.MailService;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.io.StringWriter;

/**
 * Created by albertoruvel on 25/06/17.
 */
@Component
public class MailServiceImpl implements MailService {

    private final String mailUsername = System.getProperty("mail.config.username");

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String TEMPLATE_PATH = "/templates/error-message-template.vm";
    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    @Qualifier("velocityEngine")
    private VelocityEngine velocityEngine;

    public void sendErrorEmail(ErrorMessage errorMessage) throws Exception{
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("server-error-consumer");
        simpleMailMessage.setTo(mailUsername);
        simpleMailMessage.setSubject(String.format("%s error", errorMessage.getApplicationId()));

        log.info("Will send email template: " + TEMPLATE_PATH);
        Template template = velocityEngine.getTemplate(TEMPLATE_PATH);

        VelocityContext context = new VelocityContext();
        context.put("applicationId", errorMessage.getApplicationId());
        context.put("body", errorMessage.getBody());
        context.put("date", errorMessage.getDate());

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        simpleMailMessage.setText(writer.toString());
        javaMailSender.send(simpleMailMessage);
    }
}
