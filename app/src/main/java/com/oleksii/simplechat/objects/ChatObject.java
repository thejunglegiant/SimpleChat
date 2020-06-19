package com.oleksii.simplechat.objects;

public class ChatObject {

    public String title;
    public Message lastMessage;
    public Boolean attached;

    public ChatObject(String title, Message lastMessage, Boolean attached) {
        this.title = title;
        this.lastMessage = lastMessage;
        this.attached = attached;
    }


}
