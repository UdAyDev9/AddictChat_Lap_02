package com.addictchat.model;

public class Message {
    public String msg;
    public String time;
    public String type;
    public Boolean from;


    public Message() {
    }

    public Message(String msg, String time, String type, Boolean from) {
        this.msg = msg;
        this.time = time;
        this.type = type;
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getFrom() {
        return from;
    }

    public void setFrom(Boolean from) {
        this.from = from;
    }


}
