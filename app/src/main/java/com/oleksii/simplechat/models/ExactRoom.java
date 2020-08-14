package com.oleksii.simplechat.models;

import java.util.List;

public class ExactRoom {

    private int members;
    private List<Message> messages;

    public ExactRoom(int members, List<Message> messages) {
        this.members = members;
        this.messages = messages;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
