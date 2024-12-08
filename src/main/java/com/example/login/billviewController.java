package com.example.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class billviewController implements Initializable {
    private String username;
    @FXML
    private Label bonjourusername1;
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
    private TableColumn<Bill, Integer> bill_idcol;

    @FXML
    private TableView<Bill> bill_table;

    @FXML
    private TableColumn<Bill, Timestamp> date_idcol;

    @FXML
    private TableColumn<Bill, Integer> id_plancol;

    @FXML
    private TableColumn<Bill, String> statuscol;

    @FXML
    private TableColumn<Bill, Double> total_amountcol;

    private ObservableList<Bill> billData = FXCollections.observableArrayList();

    private void loadDataFromDatabase() {
        try {
            billData.clear();
            setUsername(UserModel.getUsername());
            bonjourusername1.setText("hello "+username);
            System.out.println(username);


            String query = "SELECT id_bill, created_at, plan_id, status, total_amount " +
                    "FROM bill " +
                    "WHERE username = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, getUsername());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("id_bill"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getInt("plan_id"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total_amount")
                );
                billData.add(bill);
            }

            // Set the items in the TableView
            bill_table.setItems(billData);
        } catch (SQLException ex) {
            Logger.getLogger(billviewController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close resources in a finally block
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
        bill_idcol.setCellValueFactory(cellData -> cellData.getValue().idBillProperty().asObject());
        date_idcol.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        id_plancol.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());
        statuscol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        total_amountcol.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

        // Populate data from the database with a specific username
        loadDataFromDatabase();

    }


}
