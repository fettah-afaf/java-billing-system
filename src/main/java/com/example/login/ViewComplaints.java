package com.example.login;


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

public class ViewComplaints implements Initializable {

    private String username;
    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    private TableColumn<Complaint, Integer> complaintIdColumn;

    @FXML
    private TableColumn<Complaint, Timestamp> dateComplaintColumn;

    @FXML
    private TableColumn<Complaint, String> complaintDetailsColumn;

    @FXML
    private TableView<Complaint> tableView;

    private ObservableList<Complaint> complaintData = FXCollections.observableArrayList();

    @FXML
    private TextField usernameField;

    @FXML
    private Label messageLabel5;

    private void loadDataFromDatabase(String username) {
        try {
            complaintData.clear();

            String query = "SELECT complaint_id, date_complaint, complaint_details " +
                    "FROM complaint " +
                    "WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Complaint complaint = new Complaint(
                        resultSet.getInt("complaint_id"),
                        resultSet.getTimestamp("date_complaint"),
                        resultSet.getString("complaint_details")
                );
                complaintData.add(complaint);
            }

            // Set the items in the TableView
            tableView.setItems(complaintData);
        } catch (SQLException ex) {
            Logger.getLogger(ViewComplaints.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the database connection using your db class
        connection = db.getConnection();

        // Bind columns with corresponding properties
        complaintIdColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        dateComplaintColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        complaintDetailsColumn.setCellValueFactory(cellData -> cellData.getValue().getDetailsProperty());

        // Populate data from the database with a specific username
        loadDataFromDatabase(username);
    }

    @FXML
    void loadComplaintsButtonOnAction() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            loadDataFromDatabase(username);
            messageLabel5.setText("Complaints loaded successfully.");
        } else {
            messageLabel5.setText("Please enter a username.");
        }
    }
}



