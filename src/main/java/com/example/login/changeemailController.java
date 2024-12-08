package com.example.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class changeemailController {
    private String username;

    @FXML
    private ImageView cancelimage;

    @FXML
    private Button changeemail;

    @FXML
    private Label erroremail;

    @FXML
    private TextField newemailfx;

    @FXML
    private TextField oldemailfx;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @FXML
    public void cancelimageOnMouseClicked(MouseEvent event) {
        System.out.println("Cancel image clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void changeemailOnAction(ActionEvent event) {
        setUsername(UserModel.getUsername());
        if (!newemailfx.getText().isBlank() && !oldemailfx.getText().isBlank()) {
            String oldemail = oldemailfx.getText();
            String newemail = newemailfx.getText();

            // Assuming you have a method to get the database connection
            try (Connection connection = db.getConnection()) {
                // Create a query to retrieve the password for the given username
                String query = "SELECT email FROM users WHERE username = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, getUsername());

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            // Check if the entered old password matches the stored password
                            String storedemail = resultSet.getString("email");
                            if (oldemail.equals(storedemail)) {
                                // Update the password in the users table
                                String updateQuery = "UPDATE users SET email = ? WHERE username = ?";
                                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                    updateStatement.setString(1, newemail);
                                    updateStatement.setString(2, getUsername());
                                    updateStatement.executeUpdate();

                                    erroremail.setText("email changed successfully");
                                }
                            } else {
                                erroremail.setText("Old email is not correct");
                            }
                        } else {
                            erroremail.setText("Username not found");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
                erroremail.setText("Error changing email");
            }
        } else {
            erroremail.setText("Please fill in both old and new email");
        }
    }

    }

