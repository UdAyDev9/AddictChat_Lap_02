package com.addictchat.model;

public class Users {

  private String user_name;
  private String user_phone;
  private String user_status;
  private String user_image;
  private String user_online_status;
  private String user_id;



  public Users(String user_name, String user_phone, String user_status, String user_image,
      String user_online_status, String user_id) {
    this.user_name = user_name;
    this.user_phone = user_phone;
    this.user_status = user_status;
    this.user_image = user_image;
    this.user_online_status = user_online_status;
    this.user_id = user_id;
  }

  public Users(String user_name, String user_phone, String user_status, String user_image,
      String user_online_status) {
    this.user_name = user_name;
    this.user_phone = user_phone;
    this.user_status = user_status;
    this.user_image = user_image;
    this.user_online_status = user_online_status;
  }

  public Users(String user_name, String user_phone, String user_status, String user_image) {
    this.user_name = user_name;
    this.user_phone = user_phone;
    this.user_status = user_status;
    this.user_image = user_image;
  }

  public Users() {
  }

  public String getUser_online_status() {
    return user_online_status;
  }

  public void setUser_online_status(String user_online_status) {
    this.user_online_status = user_online_status;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getUser_phone() {
    return user_phone;
  }

  public void setUser_phone(String user_phone) {
    this.user_phone = user_phone;
  }

  public String getUser_status() {
    return user_status;
  }

  public void setUser_status(String user_status) {
    this.user_status = user_status;
  }

  public String getUser_image() {
    return user_image;
  }

  public void setUser_image(String user_image) {
    this.user_image = user_image;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }
}
