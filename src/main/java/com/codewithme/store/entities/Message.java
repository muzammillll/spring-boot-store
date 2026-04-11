package com.codewithme.store.entities;

public class Message {

    private String text;


    public Message(String text) {
        this.text = text;
    }

    // optional (good practice)
    public Message() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}