package com.example.login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class signupController implements Initializable {
    @FXML
    private Button registerbutton;
    @FXML
    private TextField usernametextfield;
    @FXML
    private TextField lastnametextfield;
    @FXML
    private TextField firstnametextfield;
    @FXML
    private PasswordField passwordtextfield;
    @FXML
    private TextField cintextfield;
    @FXML
    private TextField plan_idtextfield;
    @FXML
    private TextField emailtextfield;
    @FXML
    private ComboBox<String> genderbox;
    @FXML
    private Button backtologinbutton;
    ObservableList<String> list = FXCollections.observableArrayList("FEMALE", "MALE");


    @FXML
    private Button cancelbutton;
    @FXML
    private Label signupmessagelabel1;
    public void cancelbuttonOnAction(ActionEvent e){
        Stage stage=(Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void backtologinbuttonOnAction(ActionEvent event) {
        // Close the current (signup) window
        Stage stage = (Stage) backtologinbutton.getScene().getWindow();
        stage.close();

        try {
            // Load the login window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);

            // Create a new stage for the login window
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately (log it, show an error message, etc.)
        }
    }

    public void registerbuttonOnAction(ActionEvent event) {
        if (usernametextfield.getText().trim().isEmpty() || lastnametextfield.getText().trim().isEmpty()
                || firstnametextfield.getText().trim().isEmpty() || passwordtextfield.getText().trim().isEmpty()
                || cintextfield.getText().trim().isEmpty() || plan_idtextfield.getText().trim().isEmpty()
                || genderbox.getValue().trim().isEmpty() || emailtextfield.getText().trim().isEmpty()) {

            signupmessagelabel1.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
            signupmessagelabel1.setText("Enter all Details");

        } else {
            try (Connection connection = db.getConnection()) {
                int planId;
                try {
                    planId = Integer.parseInt(plan_idtextfield.getText());
                } catch (NumberFormatException e) {
                    // Invalid plan_id (non-numeric value)
                    signupmessagelabel1.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                    signupmessagelabel1.setText("Enter a valid numeric plan_id");
                    return;
                }
                // Check if the username already exists
                String usernameQuery = "SELECT * FROM users WHERE username = ?";
                try (PreparedStatement usernameStatement = connection.prepareStatement(usernameQuery)) {
                    usernameStatement.setString(1, usernametextfield.getText().trim());

                    try (ResultSet resultSet = usernameStatement.executeQuery()) {
                        if (resultSet.next()) {
                            // The username already exists
                            signupmessagelabel1.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                            signupmessagelabel1.setText("Username already taken. Choose another one.");
                            return; // Stop further processing
                        }
                    }
                }

                // Check if the plan_id exists in the plan table
                String planQuery = "SELECT * FROM plan WHERE plan_id = ?";
                try (PreparedStatement planStatement = connection.prepareStatement(planQuery)) {
                   // int planId = Integer.parseInt(plan_idtextfield.getText());
                    planStatement.setInt(1, planId);

                    try (ResultSet resultSet = planStatement.executeQuery()) {
                        if (!resultSet.next()) {
                            // The plan_id does not exist in the plan table
                            signupmessagelabel1.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                            signupmessagelabel1.setText("Enter a valid plan_id");
                            return; // Stop further processing
                        }
                    }
                }

                // Continue with user registration
                String query = "INSERT INTO users (username, first_name, last_name, password, gender, email, plan_id, cin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, usernametextfield.getText());
                    preparedStatement.setString(2, firstnametextfield.getText());
                    preparedStatement.setString(3, lastnametextfield.getText());
                    preparedStatement.setString(4, passwordtextfield.getText());
                    preparedStatement.setString(5, genderbox.getValue());
                    preparedStatement.setString(6, emailtextfield.getText());
                    preparedStatement.setInt(7, Integer.parseInt(plan_idtextfield.getText()));
                    preparedStatement.setString(8, cintextfield.getText());

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("User registered successfully!");
                        signupmessagelabel1.setStyle("-fx-background-color: #b3ffb3;" + "-fx-text-fill: green;");
                        signupmessagelabel1.setText("Registered");
                    } else {
                        System.out.println("Failed to register user.");
                        signupmessagelabel1.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                        signupmessagelabel1.setText("Failed");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        genderbox.setItems(list);
    }


}
