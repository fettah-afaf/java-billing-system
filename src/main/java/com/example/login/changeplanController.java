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

public class changeplanController {
    @FXML
    private ImageView cancelimage;

    @FXML
    private Button changeplan;

    @FXML
    private Label errorplan;

    @FXML
    private TextField newplanidfx;

    @FXML
    private TextField oldplanidfx;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @FXML
    void cancelimageOnMouseClicked(MouseEvent event) {
        System.out.println("Cancel image clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }


    @FXML
    void changeplanOnAction(ActionEvent event) {
        setUsername(UserModel.getUsername());
        if (!newplanidfx.getText().isBlank() && !oldplanidfx.getText().isBlank()) {
            String oldplan = oldplanidfx.getText();
            String newplan = newplanidfx.getText();

            // Assuming you have a method to get the database connection
            try (Connection connection = db.getConnection()) {
                // Check if the old plan ID matches the stored plan ID
                String checkOldPlanQuery = "SELECT plan_id FROM users WHERE username = ? AND plan_id = ?";
                try (PreparedStatement checkOldPlanStatement = connection.prepareStatement(checkOldPlanQuery)) {
                    checkOldPlanStatement.setString(1, getUsername());
                    checkOldPlanStatement.setString(2, oldplan);

                    try (ResultSet oldPlanResultSet = checkOldPlanStatement.executeQuery()) {
                        if (oldPlanResultSet.next()) {
                            // Check if the new plan ID exists in the plan table
                            String checkNewPlanQuery = "SELECT * FROM plan WHERE plan_id = ?";
                            try (PreparedStatement checkNewPlanStatement = connection.prepareStatement(checkNewPlanQuery)) {
                                checkNewPlanStatement.setString(1, newplan);

                                try (ResultSet newPlanResultSet = checkNewPlanStatement.executeQuery()) {
                                    if (newPlanResultSet.next()) {
                                        // Update the plan ID in the users table
                                        String updateQuery = "UPDATE users SET plan_id = ? WHERE username = ?";
                                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                            updateStatement.setString(1, newplan);
                                            updateStatement.setString(2, getUsername());
                                            updateStatement.executeUpdate();

                                            errorplan.setText("Plan changed successfully");
                                        }
                                    } else {
                                        errorplan.setText("New plan ID does not exist in the plan table");
                                    }
                                }
                            }
                        } else {
                            errorplan.setText("Old plan ID is not correct");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
                errorplan.setText("Error changing plan");
            }
        } else {
            errorplan.setText("Please fill in both old and new plan ID");
        }
    }



}

