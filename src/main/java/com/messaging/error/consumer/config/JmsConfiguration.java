package com.messaging.error.consumer.config;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.messaging.error.consumer.listener.ErrorMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by albertoruvel on 25/06/17.
 */
@Configuration
@EnableJms
@ComponentScan("com.messaging.error.consumer")
public class JmsConfiguration {

    //TODO: This should be changed to Spring's @Value injection *************************
    private String awsAccessKey = System.getProperty("sqs.aws.access.key");
    private final String awsSecretKey = System.getProperty("sqs.aws.secret.key");
    private final String destinationName = System.getProperty("errors.queue.name");
    //TODO: *****************************************************************************

    @Autowired
    private ErrorMessageListener errorMessageListener;

    @Bean(name = "sqsConnectionFactory")
    public SQSConnectionFactory sqsConnectionFactory() {
        SQSConnectionFactory factory = SQSConnectionFactory.builder()
                .withRegion(Region.getRegion(Regions.US_WEST_2))
                .withAWSCredentialsProvider(new AWSCredentialsProvider() {
                    public AWSCredentials getCredentials() {
                        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
                    }

                    public void refresh() {

                    }
                })
                .build();
        return factory;
    }

    @Bean(name = "defaultMessageListenerContainer")
    public DefaultMessageListenerContainer defaultMessageListenerContainer(){
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConcurrency("5");
        defaultMessageListenerContainer.setConcurrentConsumers(5);
        defaultMessageListenerContainer.setMaxConcurrentConsumers(2);
        defaultMessageListenerContainer.setIdleTaskExecutionLimit(30);
        defaultMessageListenerContainer.setIdleConsumerLimit(5);
        defaultMessageListenerContainer.setAutoStartup(true);
        defaultMessageListenerContainer.setDestinationName(destinationName);
        defaultMessageListenerContainer.setMessageListener(messageListenerAdapter());
        defaultMessageListenerContainer.setConnectionFactory(sqsConnectionFactory());
        return defaultMessageListenerContainer;
    }

    @Bean(name = "messageListenerAdapter")
    public MessageListenerAdapter messageListenerAdapter(){
        MessageListenerAdapter adapter = new MessageListenerAdapter(errorMessageListener);
        adapter.setDefaultListenerMethod("onMessage");
        adapter.setMessageConverter(null);
        return adapter;
    }

}
