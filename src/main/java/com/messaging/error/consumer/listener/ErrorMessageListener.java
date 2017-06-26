package com.messaging.error.consumer.listener;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.google.gson.Gson;
import com.messaging.error.consumer.data.ErrorMessage;
import com.messaging.error.consumer.mail.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by albertoruvel on 25/06/17.
 */
@Component
public class ErrorMessageListener {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private MailService mailService;


    //TODO: add a push notification service here?
    /**
     * Process an incoming message
     * @param message
     */
    public void onMessage(Message message){
        if(message instanceof SQSTextMessage){
            try{
                final SQSTextMessage textMessage = (SQSTextMessage)message;
                final String payload = textMessage.getText();
                mailService.sendErrorEmail(new Gson().fromJson(payload, ErrorMessage.class)); //TODO:change exception type to custom here
            }catch(JMSException ex){
                log.error(ex.getMessage());
            } catch(Exception ex){
                log.error("Could not send email: " + ex.getMessage());
            }
        }
    }
}
