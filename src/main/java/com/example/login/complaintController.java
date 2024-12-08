package com.example.login;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class complaintController implements Initializable {
    @FXML
    private TextField complainttextfx;

    @FXML
    private Label hellousername3;

    @FXML
    private AnchorPane panetoswitch;

    @FXML
    private Button submitcomplaint;
    @FXML
    private Label errorcomplaint;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    void submitOnAction(ActionEvent event) {
        try (Connection connection = db.getConnection()) {
            String complaintText = complainttextfx.getText();

            // Ensure that the complaint text is not empty
            if (!complaintText.isBlank()) {
                // Generate the current date
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

                // Insert a new record into the complaint table
                String insertComplaintQuery = "INSERT INTO complaint (date_complaint, complaint_details, username) VALUES (?, ?, ?)";
                try (PreparedStatement insertComplaintStatement = connection.prepareStatement(insertComplaintQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertComplaintStatement.setDate(1, currentDate);
                    insertComplaintStatement.setString(2, complaintText);
                    insertComplaintStatement.setString(3, getUsername());

                    // Execute the insert statement
                    insertComplaintStatement.executeUpdate();

                    // Retrieve the auto-generated complaint ID
                    ResultSet generatedKeys = insertComplaintStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int complaintId = generatedKeys.getInt(1);
                        System.out.println("Complaint submitted successfully with ID: " + complaintId);
                        errorcomplaint.setText("thank you for submitting your feedback");
                    } else {
                        System.out.println("Failed to retrieve complaint ID.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception as needed
                    System.out.println("Error submitting complaint.");
                }
            } else {
                errorcomplaint.setText("Complaint text is empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
            System.out.println("Error connecting to the database.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsername(UserModel.getUsername());
        hellousername3.setText("hello "+username);
    }
}
