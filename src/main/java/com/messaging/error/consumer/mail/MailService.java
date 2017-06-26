package com.messaging.error.consumer.mail;

import com.messaging.error.consumer.data.ErrorMessage;

/**
 * Created by albertoruvel on 25/06/17.
 */
public interface MailService {
    public void sendErrorEmail(ErrorMessage errorMessage) throws Exception;
}
