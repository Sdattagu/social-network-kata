package com.kata.socialNetwork.app.models;

import java.util.Deque;

public class Timeline {
    Deque<Message> messages;

    public Timeline(Deque<Message> messages) {
        this.messages = messages;
    }

    public Deque<Message> getMessages() {
        return messages;
    }

    public void setMessages(Deque<Message> messages) {
        this.messages = messages;
    }
}
