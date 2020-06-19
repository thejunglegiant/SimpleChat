package com.oleksii.simplechat.objects;

import java.sql.Date;

public class Message {

    private String mBody;
    private String mUsername;
    private Date mTime;

    public Message() {}

    public Message(String message, Date time) {
        this.mBody = message;
        this.mTime = time;
    }

    public String getBody() {
        return mBody;
    };

    public String getUsername() {
        return mUsername;
    };

    public Date getTime() {
        return mTime;
    }
}
