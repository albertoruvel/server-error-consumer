package com.messaging.error.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import java.util.Properties;

/**
 * Created by albertoruvel on 25/06/17.
 */
@Configuration
@ComponentScan("com.messaging.error.consumer")
public class CommonBeansConfiguration {

    //TODO: This should be changed to Spring's @Value injection *************************
    private final String mailHost = System.getProperty("mail.config.host");
    private final int mailPort = Integer.parseInt(System.getProperty("mail.config.port"));
    private final String mailUsername = System.getProperty("mail.config.username");
    private final String mailPassword = System.getProperty("mail.config.password");
    private final String mailDebug = System.getProperty("mail.config.debug");
    //TODO: *****************************************************************************

    @Bean(name = "velocityEngine")
    public VelocityEngineFactoryBean velocityEngineFactoryBean(){
        VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
        Properties velocityProperties = new Properties();
        velocityProperties.put("resource.loader", "class");
        velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngineFactoryBean.setVelocityProperties(velocityProperties);
        return velocityEngineFactoryBean;
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailHost);
        sender.setPort(mailPort);
        sender.setUsername(mailUsername);
        sender.setPassword(mailPassword);

        Properties javaMailProperties = new Properties();

        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", mailDebug);

        sender.setJavaMailProperties(javaMailProperties);
        return sender;
    }
}
