package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;



public class minipayController implements Initializable {
    private String username;
    @FXML
    private Label messagepay;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @FXML
    private Label idLabel;

    @FXML
    private TextField amountTextField;
    @FXML
    private Label dateLabel; // Add this line

    @FXML
    private Label totalAmountLabel;

    // Store the id_bill value
    private int idBill;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Additional initialization code if needed
    }

    // Method to receive and store the id_bill value
    public void setIdBill(int idBill) {
        this.idBill = idBill;
        idLabel.setText(String.valueOf(idBill));
    }
    public void setData(Bill selectedBill) {
        idLabel.setText(String.valueOf(selectedBill.getIdBill()));
        dateLabel.setText(selectedBill.getDateCreated().toString()); // Assuming DateCreated is a Timestamp
        totalAmountLabel.setText(String.valueOf(selectedBill.getTotalAmount()));

    }
    @FXML
    private void cancelimageOnMouseClicked(MouseEvent event) {
        System.out.println("Cancel image clicked");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    // Method to handle the payment insertion
    @FXML
    private void minipaybuttonOnAction(ActionEvent event) {
        try {
            if(!amountTextField.getText().isBlank()) {
                // Get the amount from the TextField
                double amount ;
                try {
                    amount= Double.parseDouble(amountTextField.getText());
                    if (amount < 0) {
                        messagepay.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                        messagepay.setText("Amount cannot be negative");
                        return;
                    }
                } catch (NumberFormatException e) {
                   
                    messagepay.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                    messagepay.setText("Enter a valid amount");
                    return;
                }

                setUsername(UserModel.getUsername());
                String text = totalAmountLabel.getText();
                double totalAmount = Double.parseDouble(text);

                if (amount == totalAmount) {

                    Connection connection = db.getConnection();

                    // Create the query to insert data into the payment table
                    String insertQuery = "INSERT INTO payment (id_bill, amount_pay) VALUES (?, ?);";

                    // Create the query to update the status in the bill table
                    String updateQuery = "UPDATE bill SET status = 'paid' WHERE id_bill = ?;";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                         PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                        // Insert payment
                        insertStatement.setInt(1, idBill);
                        insertStatement.setDouble(2, amount);
                        insertStatement.executeUpdate();

                        // Update status
                        updateStatement.setInt(1, idBill);
                        updateStatement.executeUpdate();

                        messagepay.setStyle("-fx-background-color: green;" + "-fx-text-fill: white;");
                        messagepay.setText("We are treating your payment");

                    }

                // Close the connection
                connection.close();
                } else {
                    // Display an error message if amount is not equal to totalAmount
                    messagepay.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                    messagepay.setText("Amount must be equal to Total Amount");
                }


                // Close the current stage or perform any other necessary actions
                // Stage stage = (Stage) amountTextField.getScene().getWindow();
                // stage.close();
            }else{
                messagepay.setStyle("-fx-background-color: #ffb3b3;" + "-fx-text-fill: red;");
                messagepay.setText("set your amount");
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            System.out.println("failed");
            // Handle exceptions as needed
        }
    }
}
