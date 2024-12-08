package com.example.login;

import com.example.login.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewPayments implements Initializable {

    private String username;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private TableColumn<Payment, Integer> payment_idcol;

    @FXML
    private TableColumn<Payment, Timestamp>datecol;

    @FXML
    private TableColumn<Payment, Double> amount_paycol;
    @FXML
    private TableColumn<Payment, Integer> id_billcol;

    private ObservableList<Payment> paymentData = FXCollections.observableArrayList();
    @FXML
    private TableView<Payment> payment_table;


    @FXML
    private TextField usernameField;

    @FXML
    private Label messageLabel6;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the database connection using your db class
        connection = db.getConnection();

        // Bind columns with corresponding properties
        payment_idcol.setCellValueFactory(cellData -> cellData.getValue().idPaymentProperty().asObject());
        datecol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        id_billcol.setCellValueFactory(cellData -> cellData.getValue().idBillProperty().asObject());
        amount_paycol.setCellValueFactory(cellData -> cellData.getValue().amountPaidProperty().asObject());

        // Populate data from the database with a specific username
        loadPaymentsForUsername(username);
    }

    private void loadPaymentsForUsername(String username) {
        try {
            ObservableList<Payment> paymentData = FXCollections.observableArrayList();

            // Fetch payments related to bills for the given username
            String query = "SELECT p.id_payment, p.date, p.id_bill, p.amount_pay " +
                    "FROM payment p " +
                    "JOIN bill b ON p.id_bill = b.id_bill " +
                    "WHERE b.username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Payment payment = new Payment(
                        resultSet.getInt("id_payment"),
                        resultSet.getTimestamp("date"),
                        resultSet.getInt("id_bill"),
                        resultSet.getDouble("amount_pay")
                );
                paymentData.add(payment);
            }

            // Set the items in the TableView
            payment_table.setItems(paymentData);
        } catch (SQLException ex) {
            Logger.getLogger(ViewBills.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void loadBillsButtonOnAction() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            loadPaymentsForUsername(username);
            messageLabel6.setText("Payments loaded successfully.");
        } else {
            messageLabel6.setText("Please enter a username.");
        }
    }
}
