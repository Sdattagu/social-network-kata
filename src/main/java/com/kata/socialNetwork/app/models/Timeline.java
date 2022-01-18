package com.kata.socialNetwork.app.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Timeline {
    Deque<Message> messages;

    public Timeline() {
        this.messages = new LinkedList();
    }

    public List<Message> getMessages() {
        return this.messages.stream().collect(Collectors.toList());
    }

    public void enqueue(String content, LocalTime timestamp, String messageOwnerName){
        if(content!=null && !content.equalsIgnoreCase("") && !messageOwnerName.equalsIgnoreCase("")) {
            messages.addFirst(new Message(content,timestamp, messageOwnerName));
        }
    }
}
