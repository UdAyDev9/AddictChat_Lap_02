package com.addictchat.model;

public class Requests {

  String uname;
  String req_status;
  String req_type;
  String time;
  String img;
  String userId;


  public Requests(String uname, String req_status, String req_type, String time, String img,
      String userId) {
    this.uname = uname;
    this.req_status = req_status;
    this.req_type = req_type;
    this.time = time;
    this.img = img;
    this.userId = userId;
  }

  public Requests(String uname, String req_status, String req_type, String time) {
    this.uname = uname;
    this.req_status = req_status;
    this.req_type = req_type;
    this.time = time;
  }

  public Requests(String uname, String req_status, String req_type, String time, String img) {
    this.uname = uname;
    this.req_status = req_status;
    this.req_type = req_type;
    this.time = time;
    this.img = img;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public Requests() {
  }

  public String getUname() {
    return uname;
  }

  public void setUname(String uname) {
    this.uname = uname;
  }

  public String getReq_status() {
    return req_status;
  }

  public void setReq_status(String req_status) {
    this.req_status = req_status;
  }

  public String getReq_type() {
    return req_type;
  }

  public void setReq_type(String req_type) {
    this.req_type = req_type;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
