package com.kata.socialNetwork.app.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Message {
    private String content;
    private LocalTime publishedTime;
    private String owner;

    public Message(String content, LocalTime publishedTime, String owner) {
        this.content = content;
        this.publishedTime = publishedTime;
        this.owner= owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalTime getPublishedTime() {
        return publishedTime;
    }

    public void LocalTime(LocalTime publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", publishedTime=" + publishedTime +
                '}';
    }
}
