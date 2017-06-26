package com.messaging.error.consumer.data;

/**
 * Created by albertoruvel on 25/06/17.
 */
public class ErrorMessage {
    private String body;
    private String date;
    private String applicationId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
