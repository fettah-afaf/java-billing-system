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
public class changepasswordController {
    private String username;
    @FXML
    private ImageView cancelimage;

    @FXML
    private Button changepass;

    @FXML
    private Label errorpassword;

    @FXML
    private TextField newpasswordfx;

    @FXML
    private TextField oldpasswodfx;
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
    public void changepassOnAction(ActionEvent event) {
        setUsername(UserModel.getUsername());
        if (!newpasswordfx.getText().isBlank() && !oldpasswodfx.getText().isBlank()) {
            String oldPassword = oldpasswodfx.getText();
            String newPassword = newpasswordfx.getText();

            // Assuming you have a method to get the database connection
            try (Connection connection = db.getConnection()) {
                // Create a query to retrieve the password for the given username
                String query = "SELECT password FROM users WHERE username = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, getUsername());

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            // Check if the entered old password matches the stored password
                            String storedPassword = resultSet.getString("password");
                            if (oldPassword.equals(storedPassword)) {
                                // Update the password in the users table
                                String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
                                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                    updateStatement.setString(1, newPassword);
                                    updateStatement.setString(2, getUsername());
                                    updateStatement.executeUpdate();

                                    errorpassword.setText("Password changed successfully");
                                }
                            } else {
                                errorpassword.setText("Old password is not correct");
                            }
                        } else {
                            errorpassword.setText("Username not found");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
                errorpassword.setText("Error changing password");
            }
        } else {
            errorpassword.setText("Please fill in both old and new passwords");
        }
    }
}
