package com.kata.socialNetwork.app.models;

import java.time.LocalDateTime;

public class Message {
    private String content;
    private LocalDateTime publishedDateTime;

    public Message(String content, LocalDateTime publishedDateTime) {
        this.content = content;
        this.publishedDateTime = publishedDateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedDateTime() {
        return publishedDateTime;
    }

    public void setPublishedDateTime(LocalDateTime publishedDateTime) {
        this.publishedDateTime = publishedDateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", publishedDateTime=" + publishedDateTime +
                '}';
    }
}
