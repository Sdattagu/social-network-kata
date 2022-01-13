package com.kata.socialNetwork.app.models;

public class Wall {
    Timeline timeline;

    public Wall() {
        this.timeline = new Timeline();
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}
