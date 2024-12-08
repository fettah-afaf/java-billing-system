package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class profileController implements Initializable {

    @FXML
    private Label cinfx;

    @FXML
    private Label emailfx;

    @FXML
    private Label firstnamefx;

    @FXML
    private Label genderfx;

    @FXML
    private Label lastnamefx;

    @FXML
    private Label plan_idfx;

    @FXML
    private Label usernamefx;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        // Call a method to load and display user details
        loadUserDetails();
    }

    // Method to fetch and display user details
    private void loadUserDetails() {
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, getUsername());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Set user details to the labels
                    usernamefx.setText(resultSet.getString("username"));
                    firstnamefx.setText(resultSet.getString("first_name"));
                    lastnamefx.setText(resultSet.getString("last_name"));
                    genderfx.setText(resultSet.getString("gender"));
                    emailfx.setText(resultSet.getString("email"));
                    plan_idfx.setText(resultSet.getString("plan_id"));
                    cinfx.setText(resultSet.getString("CIN"));
                } else {
                    // Handle the case where the user is not found
                    System.out.println("User not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, e.g., display an error message to the user
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsername(UserModel.getUsername());

    }
}
