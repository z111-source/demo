package com.example.database.entity;

public class LoginInfo {

    public int id;
    public String phone;
    public String password;
    public boolean remPass=false;
    public LoginInfo(){

    }


    public LoginInfo(String phone, String password, boolean remPass) {
        this.phone = phone;
        this.password = password;
        this.remPass = remPass;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", remember=" + remPass +
                '}';
    }
}

