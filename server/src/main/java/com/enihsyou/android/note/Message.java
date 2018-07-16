package com.enihsyou.android.note;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty("msg")
    String msg;

    @JsonProperty("body")
    Object body;

    public Message(String msg, Object result) {
        this.msg = msg;
        this.body = result;
    }

    public static Message text(String message) {
        return new Message(message, null);
    }

    public static Message body(Object result) {
        return new Message(null, result);
    }
}
