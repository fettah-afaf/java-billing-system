package com.example.login;

public class user {
    private String firstName;
    private String lastName;
    private String cin;
    private String email;
    private int idPlan;  // Assuming this is the plan ID associated with the user

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCin() {
        return cin;
    }

    public String getEmail() {
        return email;
    }
    public user(String firstName, String lastName, String cin, String email, int idPlan) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.email = email;
        this.idPlan = idPlan;
    }
    public int getIdPlan() {
        return idPlan;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public user() {
    }
}
