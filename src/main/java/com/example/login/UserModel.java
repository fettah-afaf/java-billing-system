package com.example.login;


public class UserModel {
    private static String username;
    private static UserModel instance;

    public UserModel(String username){
        UserModel.username = username;
    }
    public static UserModel getInstance(String username) {
        if(instance == null ){
            instance = new UserModel(username);
        }
        return instance;
    }

    public static String getUsername() {
        return username;
    }
}
