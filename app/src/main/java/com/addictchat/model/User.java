package com.addictchat.model;

public class User {

   private String id;
    private String userName;
    private String userStatus;

    private String userImage;
    private String user_online_status;

    public User() {

    }


  public User(String userName, String userStatus, String userImage,
      String user_online_status) {
    this.userName = userName;
    this.userStatus = userStatus;
    this.userImage = userImage;
    this.user_online_status = user_online_status;
  }

  public User(String userName, String userStatus, String userImage) {
    this.userName = userName;
    this.userStatus = userStatus;
    this.userImage = userImage;
  }
  public User(String userName, String userStatus) {
    this.userName = userName;
    this.userStatus = userStatus;
  }

  public String getUser_online_status() {
    return user_online_status;
  }

  public void setUser_online_status(String user_online_status) {
    this.user_online_status = user_online_status;
  }

  public String getUserImage() {
    return userImage;
  }

  public void setUserImage(String userImage) {
    this.userImage = userImage;
  }


    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
