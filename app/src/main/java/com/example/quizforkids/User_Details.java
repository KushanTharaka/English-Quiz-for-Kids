package com.example.quizforkids;

public class User_Details {

    String userEmail;
    private static final User_Details ourInstance = new User_Details();
    public static User_Details getInstance() {
        return ourInstance;
    }
    private User_Details() {
    }
    public void setData(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getData() {
        return userEmail;
    }

}
