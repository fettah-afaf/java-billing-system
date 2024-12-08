
package com.example.login;

        import javafx.beans.property.SimpleStringProperty;
        import javafx.beans.property.StringProperty;

public class UserProfile {
    private StringProperty username;
    private StringProperty lastName;
    private StringProperty firstName;
    private StringProperty gender;
    private StringProperty email;

    // Constructors
    public UserProfile() {
        this.username = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.gender = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public UserProfile(String username, String lastName, String firstName, String gender, String email) {
        this.username = new SimpleStringProperty(username);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.gender = new SimpleStringProperty(gender);
        this.email = new SimpleStringProperty(email);
    }

    // Getter and Setter methods
    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}