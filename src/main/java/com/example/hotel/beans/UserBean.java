package com.example.hotel.beans;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName; //
    private String password;
    private String userRole; //
    private String fullName;
    private String email;
    private String phone;

    public UserBean() {
    }

    public UserBean(String userId, String userName, String password, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    

    public String getRole() {
        return userRole;
    }

    public void setRole(String role) {
        this.userRole = role;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }    @Override
    public String toString() {
        return "UserBean{" +
               "userId='" + userId + '\'' +
               ", userName='" + userName + '\'' +
               // Avoid logging password
               ", userRole='" + userRole + '\'' +
               '}';
    }
} 